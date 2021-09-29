package com.acompworld.teamconnect.ui.fragments.directory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.responses.EmployeeResponse
import com.acompworld.teamconnect.databinding.FragmentEmployeesBinding
import com.acompworld.teamconnect.utils.Constants.EMPLYOYEE_ID
import com.acompworld.teamconnect.utils.Constants.PROJECT_Code
import com.acompworld.teamconnect.utils.Resource
import com.acompworld.teamconnect.utils.loadPfp
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployeeProfile : Fragment(R.layout.fragment_employees) {

    private val viewModel: DirectoryViewModel by viewModels()
    private var empID: String? = null
    private var projectCode: String? = null
    private var binding: FragmentEmployeesBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            empID = getString(EMPLYOYEE_ID).toString()
            projectCode = getString(PROJECT_Code).toString()
        }
        projectCode?.let { code -> empID?.let { empID -> viewModel.getEmployeeByID(code, empID) } }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.employee.observe({ lifecycle }) {
           handleResources(view,it)
        }

    }

    private fun handleResources(view: View, res: Resource<EmployeeResponse?>) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true

            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getEmployeeByID(projectCode!!,empID!!)
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(it: EmployeeResponse) {
        binding?.apply {
            ivPfp.loadPfp(it.profilepic)
            tvName.text = it.empname
            tvDesignation.text = "${it.department} - ${it.designation}"
            tvAddress.text = it.location
            tvEmployeeNo.text = it.empno
            tvEmail.text = it.email
            tvMobile.text = it.mobile
            tvIntercomO.text = it.intercomoffice
            tvAlternateMobNo.text = it.alternatemobile
        }

    }
}