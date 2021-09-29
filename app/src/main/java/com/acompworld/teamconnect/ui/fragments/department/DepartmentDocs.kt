package com.acompworld.teamconnect.ui.fragments.department

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.entities.Document
import com.acompworld.teamconnect.api.model.responses.DepartmentDocumentsResponse
import com.acompworld.teamconnect.databinding.FragmentDepartmentDocsBinding
import com.acompworld.teamconnect.databinding.ItemDptDocsBinding
import com.acompworld.teamconnect.ui.MainActivity
import com.acompworld.teamconnect.utils.Constants.KEY_DEPT_ID
import com.acompworld.teamconnect.utils.Constants.KEY_FILE
import com.acompworld.teamconnect.utils.Constants.KEY_RID
import com.acompworld.teamconnect.utils.Constants.PROJECT_Code
import com.acompworld.teamconnect.utils.GenericListAdapter
import com.acompworld.teamconnect.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepartmentDocs : Fragment() {
    private var binding: FragmentDepartmentDocsBinding? = null
    var mProjectcode: String? = null
    var mDeptid: Int? = null
    var mRid: Int? = null
    private val viewModel: DepartmentViewModel by viewModels()

    private fun fetchData() {

        arguments?.apply {
            mDeptid = getInt(KEY_DEPT_ID)
            mProjectcode = getString(PROJECT_Code)
            mRid = getInt(KEY_RID)
        }
        mRid ?: Log.d("omega_fragDeptDoc", "rid is null")
        mDeptid ?: Log.d("omega_fragDeptDoc", "deptid null")
        mProjectcode ?: Log.d("omega_fragDeptDoc", "projectCode null")

        mRid?.let { rId ->
            mDeptid?.let { deptid ->
                mProjectcode?.let { code ->
                    viewModel.getDepartmentDocsByRid(projectCode = code, deptId = deptid, rId = rId)
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (viewModel.deptDocs.value == null)
            fetchData()
        binding = FragmentDepartmentDocsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.deptDocs.observe({ lifecycle }) {
            handleResources(view, it)

        }

    }

    private fun handleResources(view: View, res: Resource<DepartmentDocumentsResponse>) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true

            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        fetchData()
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(it: DepartmentDocumentsResponse) {
        (requireActivity() as MainActivity).toolbar?.title = it.titleEng
        binding?.rv?.apply {
            adapter = getRvAdapter(it.documents)
        }
    }

    private fun getRvAdapter(documents: List<Document>?) = object : GenericListAdapter<Document>(
        layoutId = R.layout.item_dpt_docs,
        bind = { item, holder, itemCount ->
            val itemBinding = ItemDptDocsBinding.bind(holder.itemView)

            itemBinding.apply {
                val iconResource =
                    if (item.isdirectory == true) R.drawable.ic_folder else R.drawable.ic_file
                ivFileIcon.setImageResource(iconResource)
                tvName.text = item.titleEng
                tvDetails.text = item.extension

                root.setOnClickListener {
                    //open  folder
                    if (item.isdirectory == true) {
                        val bundle = Bundle()
                        bundle.apply {
                            putString(PROJECT_Code, mProjectcode)
                            putInt(KEY_RID, item.rid?.toInt()!!)
                            putInt(KEY_DEPT_ID, mDeptid!!)
                        }
                        findNavController().navigate(R.id.action_global_departmentDocs, bundle)

                    } else
                        findNavController().navigate(
                        R.id.action_global_webView, androidx.core.os.bundleOf(
                           Pair( KEY_FILE, item.linkurl?:"")
                        )
//                            "https://www.google.com/"
                    )
                }
            }
        }
    ) {}.apply { submitList(documents) }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}