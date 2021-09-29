package com.acompworld.teamconnect.ui.fragments.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.entities.Photo
import com.acompworld.teamconnect.api.model.responses.PhotosReponse
import com.acompworld.teamconnect.databinding.FragmentPhotosBinding
import com.acompworld.teamconnect.ui.MainActivity
import com.acompworld.teamconnect.utils.Constants.KEY_ALBUM_ID
import com.acompworld.teamconnect.utils.GenericListAdapter
import com.acompworld.teamconnect.utils.Resource
import com.acompworld.teamconnect.utils.load
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoFragment : Fragment() {
    private val viewmodel: GalleryViewModel by viewModels()
    private var binding: FragmentPhotosBinding? = null
    private var albumID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            albumID = getInt(KEY_ALBUM_ID)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (viewmodel.photos.value?.data==null)
        albumID?.let { viewmodel.getPhotos(ablumID = it) }
        binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.photos.observe({ lifecycle }) {
            handleResources(view, it)
        }

    }

    private fun handleResources(view: View, res: Resource<PhotosReponse>) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true
            }
            is Resource.error -> {
                binding?.apply {
                    progressCircular.isVisible = false
                }

                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        albumID?.let { viewmodel.getPhotos(ablumID = it) }//TODO : handle  it
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(data: PhotosReponse) {
        binding?.apply {
            rvPhotos.adapter = getAdapter(data.photos)
            (requireActivity()as MainActivity).toolbar?.title = data.name
        }
    }

    private fun getAdapter(list: List<Photo>?) = object : GenericListAdapter<Photo>(
        layoutId = R.layout.item_photos,
        bind = { item, holder, itemCount ->
            com.acompworld.teamconnect.databinding.ItemPhotosBinding.bind(holder.itemView).apply {
                ivThumbnail.load(item.photopath.toString())
            }
        }
    ) {}.apply { submitList(list) }
}