package com.acompworld.teamconnect.ui.fragments.department

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.entities.Deptlist
import com.acompworld.teamconnect.api.model.responses.DepartmentListResponse
import com.acompworld.teamconnect.databinding.FragmentDepartmentListBinding
import com.acompworld.teamconnect.ui.MainViewModel
import com.acompworld.teamconnect.utils.Constants.PROJECT_Code
import com.acompworld.teamconnect.utils.Constants.KEY_DEPARTMENT
import com.acompworld.teamconnect.utils.GenericListAdapter
import com.acompworld.teamconnect.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepartmentList : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var binding: FragmentDepartmentListBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDepartmentListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.departments.observe({lifecycle}){
            handleResources(view,it)
        }

    }

    private fun handleResources(view: View, res: Resource<DepartmentListResponse?>) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true

            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getDepartments(viewModel.projectCode!!)
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.searchingFun = {
            viewModel.getDepartments(search = it)
        }
    }

    private fun setUpViews(it: DepartmentListResponse) {
        binding?.apply {
            if (rvDeptList.adapter == null) {
                rvDeptList.layoutManager= LinearLayoutManager(requireContext())
                rvDeptList.adapter = getAdapter(it.deptlist)
            }
            else (rvDeptList.adapter as GenericListAdapter<Deptlist>).submitList(it.deptlist)
        }
    }

    private fun getAdapter(list: List<Deptlist>) =
        object : GenericListAdapter<Deptlist>(
            layoutId = R.layout.item_dept_list,
            bind = { item, holder, itemCount ->
                val itemBinding = com.acompworld.teamconnect.databinding.ItemDeptListBinding.bind(holder.itemView)
                            itemBinding.apply {
                                tvDeptName.text = item.department
                                root.setOnClickListener {
                                    findNavController().navigate(R.id.action_nav_departments_to_departmentInfo,
                                         bundleOf(Pair( KEY_DEPARTMENT,item )).
                                         also { it.putString(PROJECT_Code,viewModel.projectCode)
                                         }
                                    )
                                }
                            }
            }
        ){}.apply{
            submitList(list)
        }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
