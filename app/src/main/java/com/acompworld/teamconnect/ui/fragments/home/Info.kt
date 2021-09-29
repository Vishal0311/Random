package com.acompworld.teamconnect.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.responses.InfoResponse
import com.acompworld.teamconnect.databinding.FragmentInfoBinding
import com.acompworld.teamconnect.ui.MainActivity
import com.acompworld.teamconnect.utils.Constants.KEY_FILE
import com.acompworld.teamconnect.utils.Constants.KEY_RID
import com.acompworld.teamconnect.utils.Constants.KEY_SECTION
import com.acompworld.teamconnect.utils.Resource
import com.acompworld.teamconnect.utils.timeStamp2
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Info : Fragment() {
    private var rId: Int? = null
    private val viewModel: HomeViewModel by viewModels()
    private var binding: FragmentInfoBinding? = null
    private var _type : Section?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            rId = getInt(KEY_RID)
            _type = getSerializable(KEY_SECTION) as Section?
        }

        (requireActivity() as MainActivity).apply {
            toolbar?.isVisible = false
            footer?.isVisible = false
        }

        _type?: Log.d("omega_info","tyoe is null")
        rId?:Log.d("omega_info","rid is  null")

        viewModel.apply {
            if (info.value?.data == null && rId != null && _type != null)
                getinfo(section = _type!!,  rid = rId!!)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.info.observe({ lifecycle }) {
            handleResources(view, it)
        }

        binding?.btnClose?.setOnClickListener{
            findNavController().popBackStack()
        }

    }

    private fun handleResources(view: View, res: Resource<InfoResponse>) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true

            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getinfo(rid = rId!!,section = _type!!)
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(res: InfoResponse) {

        binding?.apply {
            tvTitle.text = res.type
            tvInfoTitle.text = res.titleEng
            tvPostedOn.timeStamp2 =res.postedon.toString()
            tvPostedOn.text = "Posted on ${tvPostedOn.text}"
            tvPostedBy.text = "Posted by ${res.postedby}"

            if (!res.attachmentTitle1.isNullOrBlank()) {
                containerAttachment1.isVisible = true
                tvFile1.text = res.attachmentTitle1
                containerAttachment1.setOnClickListener {
                    findNavController().navigate(R.id.action_global_webView, bundleOf(Pair(KEY_FILE, res.attachmentPath1)))
                }
            }
            if (!res.attachmentTitle2.isNullOrBlank()) {
                containerAttachment2.isVisible = true
                tvFile2.text = res.attachmentTitle2
                containerAttachment2.setOnClickListener {
                    findNavController().navigate(R.id.action_global_webView, bundleOf(Pair(KEY_FILE, res.attachmentPath2)))
                }
            }

            if (!res.attachmentTitle3.isNullOrBlank()) {
                containerAttachment3.isVisible = true
                tvFile3.text = res.attachmentTitle3
                containerAttachment3.setOnClickListener {
                    findNavController().navigate(R.id.action_global_webView, bundleOf(Pair(KEY_FILE, res.attachmentPath3)))
                }
            }
            if (!res.attachmentTitle4.isNullOrBlank()) {
                containerAttachment4.isVisible = true
                tvFile4.text = res.attachmentTitle4
                containerAttachment4.setOnClickListener {
                    findNavController().navigate(R.id.action_global_webView, bundleOf(Pair(KEY_FILE, res.attachmentPath4)))
                }
            }

            if (!res.attachmentTitle5.isNullOrBlank()) {
                containerAttachment5.isVisible = true
                tvFile5.text = res.attachmentTitle5
                containerAttachment5.setOnClickListener {
                    findNavController().navigate(R.id.action_global_webView, bundleOf(Pair(KEY_FILE, res.attachmentPath5)))
                }
            }
        }
    }


    override fun onDestroy() {
        (requireActivity() as MainActivity).apply {
            toolbar?.isVisible = true
            footer?.isVisible = true
        }
        binding = null
        super.onDestroy()
    }
}