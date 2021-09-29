package com.acompworld.teamconnect.ui.fragments.directory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.acompworld.teamconnect.api.model.responses.ContactResponse
import com.acompworld.teamconnect.databinding.FragmentTelephoneContactBinding
import com.acompworld.teamconnect.utils.Constants.PROJECT_Code
import com.acompworld.teamconnect.utils.Constants.TELEPHONE_CONTACT_ID
import com.acompworld.teamconnect.utils.Resource
import com.acompworld.teamconnect.utils.loadPfp
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TelePhoneContact : Fragment() {
    private val viewModel: DirectoryViewModel by viewModels()
    private  var empID : String?= null
    private var projectCode:String?= null
    private var  binding  : FragmentTelephoneContactBinding? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO : Override directory ViewModel's constructor
        arguments?.apply {
            empID = getString(TELEPHONE_CONTACT_ID).toString()
            projectCode =getString(PROJECT_Code).toString()
        }
        projectCode?.let { code-> empID?.let { empID -> viewModel.getContactByID(code, empID) } }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTelephoneContactBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.contact.observe({lifecycle}){
      handleResources(view,it)
        }

    }

    private fun handleResources(view: View, res: Resource<ContactResponse?>) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true

            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_INDEFINITE).apply {
                    setAction("Retry") {
                        viewModel.getEmployeeByID(projectCode!!,empID!!)
                    }
                    setAction("Back"){findNavController().popBackStack()}
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(it: ContactResponse) {
        binding?.apply {
            tvName.text = it.location
            tvAbout.text = it.category
            tvAddress.text = it.address
            tvEmail.text = it.email
            tvIntercome.text = it.intercome
            tvPhone.text = it.phone
            ivPfp.loadPfp(it.icon)
        }
    }
}