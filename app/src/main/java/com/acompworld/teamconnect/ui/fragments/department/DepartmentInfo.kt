package com.acompworld.teamconnect.ui.fragments.department

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.entities.Bannerlist
import com.acompworld.teamconnect.api.model.entities.Deptlist
import com.acompworld.teamconnect.api.model.entities.Documenttab
import com.acompworld.teamconnect.api.model.entities.Update
import com.acompworld.teamconnect.api.model.responses.DepartmentInfoResponse
import com.acompworld.teamconnect.databinding.FragmentDepartmentInfoBinding
import com.acompworld.teamconnect.databinding.ItemAboutViewpagerBinding
import com.acompworld.teamconnect.databinding.ItemDepartmentUpdatesBinding
import com.acompworld.teamconnect.ui.MainActivity
import com.acompworld.teamconnect.utils.*
import com.acompworld.teamconnect.utils.Constants.KEY_DEPARTMENT
import com.acompworld.teamconnect.utils.Constants.KEY_RID
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepartmentInfo : Fragment() {

    private val viewModel: DepartmentViewModel by viewModels()
    private var binding: FragmentDepartmentInfoBinding? = null

    var deptItem: Deptlist? = null
    var projectcode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            deptItem = getSerializable(KEY_DEPARTMENT) as Deptlist
            projectcode = getString(Constants.PROJECT_Code)
        }

        deptItem?.let { item ->
            projectcode?.let { code ->
                if(viewModel.departmentInfo.value == null )
                viewModel.getDepartmentByID(code, item.deptid)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDepartmentInfoBinding.inflate(inflater, container, false)
        return binding?.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).toolbar?.let {
            it.title = deptItem?.department
        }
        viewModel.departmentInfo.observe({ lifecycle }) {
            handleResources(view, it)
        }
    }

    private fun handleResources(view: View, res: Resource<DepartmentInfoResponse?>) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true

            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {

                        projectcode?.let { code ->
                            deptItem?.let { dept ->
                                viewModel.getDepartmentByID(code, dept.deptid)
                            }
                        }
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(data: DepartmentInfoResponse) {
        binding?.apply {
            tvName.text = data.department
            tvAbout.text = data.aboutdeptEng
            tvVision.text = data.visionEng
            tvMission.text = data.missionEng
            setUpBanners(data.bannerlist)
            setUpUpdates(data.updates)
            setUpDocs(data.documenttabs)
            bundleOf()
        }
    }

    private fun setUpDocs(list: List<Documenttab>?) {
        binding?.apply { rvDocs.adapter = getDocsAdapter(list) }
    }

    private fun getDocsAdapter(list: List<Documenttab>?) =
        object : GenericListAdapter<Documenttab>(
            layoutId = R.layout.item_dept_info_docs,
            bind = { item, holder, itemCount ->
                val itemBinding =
                    com.acompworld.teamconnect.databinding.ItemDeptInfoDocsBinding.bind(holder.itemView)
                itemBinding.apply {
                    tvFile.text = item.titleEng
                    root.setOnClickListener {
                        val bundle = Bundle()
                        bundle.apply {
                            putSerializable(KEY_RID, item.rid)
                            putString(Constants.PROJECT_Code, projectcode)
                            putInt(Constants.KEY_DEPT_ID, deptItem?.deptid!!)
                        }
                        findNavController().navigate(
                            R.id.action_global_departmentDocs, bundle
                        )
                    }
                }
            }
        ) {}.apply { submitList(list) }


    private fun setUpUpdates(updates: List<Update>?) {
        binding?.apply {
            rvUpdates.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = getUpdatesAdapter(updates)
            }
        }
    }


    private fun setUpBanners(bannerlist: List<Bannerlist>?) {
        binding?.apply {
            rvImg.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvImg.adapter = getBannersAdapter(bannerlist)
            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(rvImg)
            indicator.attachToRecyclerView(rvImg, pagerSnapHelper)
        }
    }

    private fun getBannersAdapter(bannerlist: List<Bannerlist>?) =
        object : GenericListAdapter<Bannerlist>(
            layoutId = R.layout.item_about_viewpager,
            bind = { item, holder, itemCount ->
                val itemBinding =
                    ItemAboutViewpagerBinding.bind(holder.itemView)
                itemBinding.apply {
                    iv.load(item.bannerfile.toString())
                    tv.text = item.captionEng
                }
            }
        ) {}.apply { submitList(bannerlist) }

    private fun getUpdatesAdapter(updates: List<Update>?) = object : GenericListAdapter<Update>(
        layoutId = R.layout.item_department_updates,
        bind = { item, holder, itemCount ->
            val itemBinding = ItemDepartmentUpdatesBinding.bind(holder.itemView)
            itemBinding.apply {
                tvDesp.text = item.descriptionEng
                item.postedon?.let { tvDate.timeStamp = it }
            }
        }
    ) {}.apply { submitList(updates) }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}