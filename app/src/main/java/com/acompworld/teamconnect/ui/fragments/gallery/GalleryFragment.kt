package com.acompworld.teamconnect.ui.fragments.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.entities.Album
import com.acompworld.teamconnect.api.model.responses.GalleryResponse
import com.acompworld.teamconnect.databinding.FragmentGalleryBinding
import com.acompworld.teamconnect.databinding.ItemGalleryBinding
import com.acompworld.teamconnect.ui.MainViewModel
import com.acompworld.teamconnect.utils.GenericListAdapter
import com.acompworld.teamconnect.utils.Resource
import com.acompworld.teamconnect.utils.Resource.*
import com.acompworld.teamconnect.utils.load
import com.acompworld.teamconnect.utils.timeStamp2
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment() {
    private var binding: FragmentGalleryBinding? = null
    private lateinit var viewmodel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.apply {
            if (gallery.value?.data == null && currentYear == getCurrYear()) getGallery(projectCode.toString(), getCurrYear())

            gallery.observe({ lifecycle }) { res ->
                handleResources(view, res)
            }
        }
        initialSetUp()
    }

    private fun initialSetUp() {
        binding?.apply{
            tvYear.text = viewmodel.currentYear.toString()
            actionNext.setOnClickListener {
                viewmodel.apply {
                    if (viewmodel.currentYear != getCurrYear()) {
                        tvYear.text = (++currentYear).toString()
                        getGallery(year = currentYear)
                    }
                }

            }
            actionPrev.setOnClickListener {
                viewmodel.apply {
                    if (currentYear <= getCurrYear()) {
                        tvYear.text = (--viewmodel.currentYear).toString()
                        getGallery(year = viewmodel.currentYear)
                    }
                }
            }
        }
    }

    private fun handleResources(view: View, res: Resource<GalleryResponse?>) {
        when (res) {
            is loading -> {
                binding?.apply {
                    progressCircular.isVisible = true
                    rv.adapter = null
                }

            }
            is error -> {
                binding?.apply {
                    progressCircular.isVisible = false
                }

                viewmodel.visited()

                if (!res.msg.isNullOrBlank()){
                    Snackbar.make(view, res.msg, Snackbar.LENGTH_LONG)
                        .apply {
                            setAction("Retry") {
                                viewmodel.getGallery() //TODO : handle  it efficiently
                            }
                        }.show()
                }
            }
            is success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(data: GalleryResponse) {
        binding?.apply {
            rv.isFocusable = false
            tvYear.text = viewmodel.currentYear.toString()
            rv.adapter = getRvAdapter(data.albums)

        }
    }


    private fun getRvAdapter(albums: List<Album>?) =
        object : GenericListAdapter<Album>(
            layoutId = R.layout.item_gallery,
            bind = { item, holder, itemCount ->
                val itemBinding = ItemGalleryBinding.bind(holder.itemView)
                itemBinding.apply {
                    tvNoOfTotalPhotos.text = "$itemCount Photos"
                    tvTitle.text = item.name
                    tvSubtitle.timeStamp2 = item.postedon.toString()
                    tvSubtitle.text = "Posted on ${tvSubtitle.text}"
                    ivThumbnail.load(item.firstphotopath.toString())

                    root.setOnClickListener {
                        item.albumid?.let { albumId ->
                            findNavController().navigate(
                                R.id.action_global_photoFragment,
                                androidx.core.os.bundleOf(
                                    Pair(
                                        com.acompworld.teamconnect.utils.Constants.KEY_ALBUM_ID,
                                        albumId
                                    )
                                )
                            )
                        }
                    }
                }
            }
        ) {}.apply { submitList(albums) }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}