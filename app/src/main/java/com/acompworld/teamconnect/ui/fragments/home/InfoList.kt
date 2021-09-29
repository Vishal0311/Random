package com.acompworld.teamconnect.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.entities.FeedItem
import com.acompworld.teamconnect.api.model.responses.InfoListResponse
import com.acompworld.teamconnect.databinding.FragmentInfolistBinding
import com.acompworld.teamconnect.databinding.ItemInfolistBinding
import com.acompworld.teamconnect.ui.MainActivity
import com.acompworld.teamconnect.utils.Constants.KEY_RID
import com.acompworld.teamconnect.utils.Constants.KEY_SECTION
import com.acompworld.teamconnect.utils.GenericListAdapter
import com.acompworld.teamconnect.utils.Resource
import com.acompworld.teamconnect.utils.timeStamp2
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoList : Fragment() {

    private var binding: FragmentInfolistBinding? = null
    private val viewModel: HomeViewModel by viewModels()
    private var _type: Section? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            _type = getSerializable(KEY_SECTION) as Section?
        }

        if (viewModel.infoList.value?.data == null && _type != null) viewModel.getinfoList(type = _type!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInfolistBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _type?.let {
            viewModel.infoList.observe({ lifecycle }) {
                handleResources(view, it)
            }
        }
    }

    private fun handleResources(view: View, res: Resource<InfoListResponse>) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true

            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getinfoList(type = _type!!)
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(response: InfoListResponse) {

        (requireActivity() as MainActivity).toolbar?.title = response.type

        binding?.rvInfolist?.apply {
            adapter = getRVAdapter(response.list)
        }

    }

    private fun getRVAdapter(list: List<FeedItem>?) = object : GenericListAdapter<FeedItem>(
        layoutId = R.layout.item_infolist,
        bind = { item, holder, itemCount ->
            ItemInfolistBinding.bind(holder.itemView).apply {
                tvName.text = item.titleEng
                tvDetails.timeStamp2 = item.postedon.toString()
                tvDetails.text = "${item.type} | ${item.postedby} | ${tvDetails.text}"
                tvNoOfAttachments.text = item.totalAttachments
                ivLabel.isVisible = (!item.totalAttachments.isNullOrBlank())
                root.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_global_nav_info, bundleOf(Pair(KEY_RID, item.rid)).also {
                            it.putSerializable(KEY_SECTION, _type)
                        }
                    )
                }
            }

        }
    ) {}.apply { submitList(list) }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}