package com.acompworld.teamconnect.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.SearchResponse
import com.acompworld.teamconnect.api.model.entities.Bannerlist
import com.acompworld.teamconnect.api.model.entities.FeedItem
import com.acompworld.teamconnect.api.model.responses.MyWallResponse
import com.acompworld.teamconnect.databinding.FragmentHomeBinding
import com.acompworld.teamconnect.databinding.HomeRvItem1Binding
import com.acompworld.teamconnect.databinding.ItemInfolistBinding
import com.acompworld.teamconnect.databinding.RvItem3Binding
import com.acompworld.teamconnect.ui.MainViewModel
import com.acompworld.teamconnect.utils.*
import com.acompworld.teamconnect.utils.Constants.KEY_SECTION
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var bannerBinding: HomeRvItem1Binding? = null
    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.search.observe({ lifecycle }) {
            handleSearchResources(view, it)
        }


        viewModel.pendingQuery.observe({ lifecycle }) {
            _binding?.apply {
                scrollView.isVisible = it.isNullOrBlank()
            }
        }
        viewModel.searchingFun = { keyword ->
            viewModel.search(search = keyword)

        }
        viewModel.myWall.observe({ lifecycle }) { myWall ->
            handleResources(view, myWall)
        }


    }

    private fun handleSearchResources(view: View, res: Resource<SearchResponse>?) {
        when (res) {
            is Resource.loading -> {
                _binding?.progressCircular?.isVisible = true

            }
            is Resource.error -> {
                _binding?.progressCircular?.isVisible = false
                res.data?.let { setUpRvSearch(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getMyWall()
                    }
                }.show()
            }
            is Resource.success -> {
                setUpRvSearch(res.data!!)
                _binding?.progressCircular?.isVisible = false
            }
        }
    }


    private fun setUpRvSearch(res: SearchResponse) {

        _binding?.apply {

            if (rvSearch.adapter == null) {
                rvSearch.adapter = getSearcAdapter(res.data)
            } else (rvSearch.adapter as GenericListAdapter<FeedItem>).submitList(res.data)
        }
    }

    private fun getSearcAdapter(list: List<FeedItem>?) = object : GenericListAdapter<FeedItem>(
        layoutId = R.layout.item_infolist,
        bind = { item, holder, itemCount ->
            ItemInfolistBinding.bind(holder.itemView).apply {
                tvName.text = item.titleEng
                tvDetails.timeStamp2 = item.postedon.toString()
                tvDetails.text = "${item.type} | ${item.postedby} | ${tvDetails.text}"
                tvNoOfAttachments.text = item.totalAttachments
                ivLabel.isVisible = (!item.totalAttachments.isNullOrBlank())
                root.setOnClickListener {

                    var _type: Section? = when {
                        item.type.equals("circular", true) -> Section.CIRCULAR
                        item.type.equals("information", true) -> Section.INFORMATION
                        item.type.equals("news", true) -> Section.NEWS
                        item.type.equals("notice", true) -> Section.NOTICE
                        item.type.equals("event", true) -> Section.EVENT
                        else -> null
                    }
                    findNavController().navigate(
                        R.id.action_global_nav_info,
                        bundleOf(Pair(Constants.KEY_RID, item.rid)).also {
                            _type?.let { type -> it.putSerializable(KEY_SECTION, type) }
                        }
                    )
                }
            }
        }
    ) {}.apply { submitList(list) }


    private fun handleResources(view: View, res: Resource<MyWallResponse?>) {
        when (res) {
            is Resource.loading -> {
                _binding?.progressCircular?.isVisible = true

            }
            is Resource.error -> {
                _binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getMyWall()
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                _binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(myWall: MyWallResponse) {
        //performing initial moves...
        defaultSetUp(myWall)
        setUpBanners(myWall)
        initializeGenerationData(myWall)
        setUpToggleBtns(myWall)
    }

    private fun defaultSetUp(myWall: MyWallResponse) {
        _binding?.apply {
            if (rv3.adapter == null) {
                toggleBtnsGrp.selectButton(viewModel.HOME_TOGGLE_BTN_ID)
                rv3.adapter = myWall.latesupdates?.let { getFeedAdapter(it) }
            }
            projectName.text = myWall.projectname
        }
    }

    private fun initializeGenerationData(myWall: MyWallResponse) {
        _binding?.apply {
            containerGenerationData.isVisible = !myWall.generationData.isNullOrEmpty()
            myWall.generationData?.let { list ->
                val item = list[0]
                tvRunningUnit.text = item.runningunits
                tvTotalUnit.text = "/${item.totalunits}"
                splitString(item.generation, tvGenrationDataUnit, tvGenrationData)
                tvPlf.text = item.plf.removeSuffix("%")
                tvFreq.text = item.freq
            }
        }
    }

    private fun splitString(str: String, tvAlphabetic: TextView, tvNumeric: TextView) {
        val alpha = StringBuffer()
        val num = StringBuffer()
        val special = StringBuffer()
        for (i in str.indices) {
            when {
                Character.isDigit(str[i]) -> num.append(str[i])
                Character.isAlphabetic(str[i].code) -> alpha.append(str[i])
                else -> special.append(str[i])
            }
        }
        tvNumeric.text = num
        tvAlphabetic.text = if (alpha.isNotBlank()) alpha.toString().lowercase() else special
    }

    private fun setUpBanners(myWall: MyWallResponse) {
        _binding?.apply {
            rv1.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rv1.adapter = getBannerAdapter(myWall.bannerlist)
        }
    }

    private fun setUpToggleBtns(myWall: MyWallResponse) {
        _binding?.apply {
            toggleBtnsGrp.setOnSelectListener { button: ThemedButton ->
                viewModel.HOME_TOGGLE_BTN_ID = button.id
                when (button.id) {
                    R.id.tag_all ->
                        rv3.adapter = myWall.latesupdates?.let { getFeedAdapter(it) }

                    R.id.tag_circular ->
                        rv3.adapter = myWall.circulars?.let { getFeedAdapter(it) }


                    R.id.tag_notices ->
                        rv3.adapter = myWall.notices?.let { getFeedAdapter(it) }


                    R.id.tag_news ->
                        myWall.news?.let { getFeedAdapter(it) }


                    R.id.tag_others ->
                        rv3.adapter = myWall.others?.let { getFeedAdapter(it) }

                }
            }

            labelViewMore.setOnClickListener {
                val type = when (viewModel.HOME_TOGGLE_BTN_ID) {
                    R.id.tag_all -> Section.INFORMATION
                    R.id.tag_circular -> Section.CIRCULAR
                    R.id.tag_news -> Section.NEWS
                    R.id.tag_notices -> Section.NOTICE
                    R.id.tag_others -> Section.EVENT
                    else -> Section.CIRCULAR  // never gonna happen
                }
                findNavController().navigate(
                    R.id.action_nav_home_to_infoList,
                    bundleOf(Pair(KEY_SECTION, type))
                )
            }
        }
    }

    private fun getFeedAdapter(list: List<FeedItem>) =
        object : GenericListAdapter<FeedItem>(
            layoutId = R.layout.rv_item_3,
            bind = { item, holder, itemCount ->
                val feedBinding = RvItem3Binding.bind(holder.itemView)
                feedBinding.apply {
                    item.postedon?.let { tvDate.timeStamp = it }
                    tvTitle.text = item.titleEng
                }
            }
        ) {}.apply {
            submitList(list)
        }

    private fun getBannerAdapter(bannerlist: List<Bannerlist>) =
        object : GenericListAdapter<Bannerlist>(
            layoutId = R.layout.home_rv_item1,
            bind = { item, holder, itemCount ->
                bannerBinding = HomeRvItem1Binding.bind(holder.itemView)
                bannerBinding?.apply {
                    ivThumbnail.load(item.bannerfile.toString())
                    tvDesp.text = item.captionEng
                }
            }
        ) {}.apply {
            submitList(bannerlist)
        }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}