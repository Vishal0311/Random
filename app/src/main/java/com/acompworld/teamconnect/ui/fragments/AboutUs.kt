package com.acompworld.teamconnect.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.entities.Image
import com.acompworld.teamconnect.api.model.responses.AboutRespone
import com.acompworld.teamconnect.databinding.FragmentAboutUsBinding
import com.acompworld.teamconnect.databinding.ItemAboutViewpagerBinding
import com.acompworld.teamconnect.ui.MainViewModel
import com.acompworld.teamconnect.utils.GenericListAdapter
import com.acompworld.teamconnect.utils.Resource
import com.acompworld.teamconnect.utils.load
import com.google.android.material.snackbar.Snackbar
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton


class AboutUs : Fragment() {


    private lateinit var viewModel: MainViewModel
    private var binding: FragmentAboutUsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutUsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.about.observe({ lifecycle }) { data ->
            handleResources(view, data)
        }

        binding!!.toggleBtnsGrp.selectButton(viewModel.ABOUT_TOGGLE_BTN_ID)
    }

    private fun handleResources(view: View, res: Resource<AboutRespone?>?) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true
            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getMyWall()
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }

    }

    private fun setUpViews(data: AboutRespone) {
        setdefaultBehaviour(data)
        setUpRv(data.images)
        setUpToggleBtns(data)
    }

    private fun setdefaultBehaviour(data: AboutRespone) {
        binding?.apply {
            tvCompleteDesp.text = data.aboutusEng
            tvShortDesp.text = data.aboutusEng
            containerIntro.setOnClickListener {
                viewModel.readMore.postValue(!viewModel.readMore.value!!)
            }

            viewModel.readMore.observe({ lifecycle }) { short ->
                containerShortDesp.isVisible = short
                tvCompleteDesp.isVisible = !short
            }
            tvToggleStatus.text = data.visionEng
            tvAddress.text = data.addressEng
            tvFlight.text = data.reachviaflightEng
            tvRoad.text = data.reachviaroadEng
            tvStation.text = data.reachviatrainEng


            //TODO Display MAP
            data.googlemap?.let{ it->
                webView.apply{
                    settings.javaScriptEnabled = true
                    val DESKTOP_USER_AGENT =
                        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36"
                    settings.userAgentString = DESKTOP_USER_AGENT
                   webChromeClient = WebChromeClient()
                   webViewClient = WebViewClient()
                    loadUrl(it)
                }

            }


        }
    }

    private fun setUpToggleBtns(data: AboutRespone) {
        binding?.apply {
            toggleBtnsGrp.setOnSelectListener { button: ThemedButton ->
                viewModel.ABOUT_TOGGLE_BTN_ID = button.id
                    when (button.id) {
                    R.id.tag_vision -> {
                        tvToggleStatus.text = data.visionEng
                    }
                    R.id.tag_mission -> {
                        tvToggleStatus.text = data.missionEng
                    }
                    R.id.tag_core_values -> {
                        tvToggleStatus.text = data.corevaluesEng
                    }
                }
            }
        }

    }

    private fun setUpRv(images: List<Image>) {
        binding?.apply {
            rvImg.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvImg.adapter = getAdapter(images)
            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(rvImg)
            indicator.attachToRecyclerView(rvImg, pagerSnapHelper)
        }
    }

    private fun getAdapter(images: List<Image>) = object : GenericListAdapter<Image>(
        layoutId = R.layout.item_about_viewpager,
        bind = { item, holder, itemCount ->
            val itemBinding = ItemAboutViewpagerBinding.bind(holder.itemView)
            itemBinding.apply {
                iv.load(item.imagepath)
            }
        }
    ) {}.apply {
        submitList(images)
    }




    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}