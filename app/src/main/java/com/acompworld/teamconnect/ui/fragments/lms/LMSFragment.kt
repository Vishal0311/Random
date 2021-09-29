package com.acompworld.teamconnect.ui.fragments.lms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.acompworld.teamconnect.api.model.responses.*
import com.acompworld.teamconnect.databinding.FragmentLmsBinding
import com.acompworld.teamconnect.utils.Constants
import com.acompworld.teamconnect.utils.Resource
import com.acompworld.teamconnect.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LMSFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var viewModel: LMSViewModel

    private var projectCode: String? = null

    private var binding: FragmentLmsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LMSViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(viewModel.lms.value == null)
            fetchData()
        binding = FragmentLmsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.lms.observe({ lifecycle }) { handleResources(view, it) }
    }

    override fun onResume() {
        /*viewModel.searchingFun = {
            viewModel.getDirectory(search = it)
        }*/
        super.onResume()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext= context
    }

    private fun handleResources(view: View, res: Resource<LMSResponse?>) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true

            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                //res.data?.let { setUpViews(it) }
                //res.data?.let { it.data?.let { setUpViews(it.) }  }
                /*Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getDirectory()
                    }
                }.show()*/
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(lmsResponse: LMSResponse) {
        /*val recyclerView: RecyclerView = binding?.rvLms!!
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.VERTICAL, false)
            adapter = ParentAdapter(ParentDataFactory.getParents(40))
        }*/
        binding?.rvLms?.adapter = LMSAdapter(viewModel, lmsResponse.data)
    }

    private fun fetchData() {
        val savedUser = SharedPref.getSavedUserData(mContext)!!;
        viewModel.getLMS(savedUser.projectCode, savedUser.userid)
    }
}