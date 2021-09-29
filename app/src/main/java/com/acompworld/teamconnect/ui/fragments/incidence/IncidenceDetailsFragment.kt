package com.acompworld.teamconnect.ui.fragments.incidence

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.responses.IncidenceDetailResponse
import com.acompworld.teamconnect.databinding.FragmentIncidenceDetailsBinding
import com.acompworld.teamconnect.utils.Constants
import com.acompworld.teamconnect.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncidenceDetailsFragment : Fragment() {

    private val viewModel: IncidenceViewModel by viewModels()
    private var binding: FragmentIncidenceDetailsBinding? = null
    private var ID: Int =-1
    private var title: String = ""
    private var date: String = ""
    private var time: String = ""
    private var area: String = ""
    private var type: String = ""
    private var detail: String = ""


    //    var incidenceItem: IncidenceDetail? = null
    var projectcode: String? = null
    var incidenceID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        arguments?.apply {
            //         incidenceItem=getSerializable("KEY_INCIDENCE_LIST") as IncidenceDetail
            projectcode = getString(Constants.PROJECT_Code)
            incidenceID = getInt("incidenceId")
        }
        //     incidenceItem?.let { item->
        projectcode?.let { code ->
            incidenceID?.let { ID ->
                if (viewModel.incidenceDetail.value == null) {
                    viewModel.getIncidenceDetailById(code, ID)
                }

            }

            //}

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIncidenceDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.incidenceDetail.observe({ lifecycle }) {
            handleResources(view, it)
        }
    }

    private fun handleResources(view: View, res: Resource<IncidenceDetailResponse?>?) {
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
                            incidenceID?.let { ID ->
                                viewModel.getIncidenceDetailById(code, ID)
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

    private fun setUpViews(data: IncidenceDetailResponse) {
        binding?.apply {

            ID = data.data.incidenceid
            title = data.data.title
            date = data.data.date
            time = data.data.time
            area = data.data.area
            type = data.data.type
            detail = data.data.details

            tvIncidencePlace.text = data.data.area
            tvIncidenceArea.text = data.data.area
            tvIncidenceTitle.text = data.data.title
            tvIncidenceType.text = data.data.type
            tvIncidenceDetail.text = data.data.details

            val place: String = data.data.date
            val time: String = time
            val final = "$place | $time"

            tvIncidenceDate.text = final


        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.incidence_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var ids = item.getItemId()

        if (ids == R.id.incidenceDetail_menu_edit) {
            val bundle = Bundle()
            bundle.apply {
                putInt("id",ID)
                putString("title", title)
                putString("title", title)
                putString("date", date)
                putString("time", time)
                putString("area", area)
                putString("type", type)
                putString("detail", detail)

            }
            findNavController().navigate(R.id.action_nav_incidenceDetail_to_nav_incidenceEdit,
                bundle)
        }

        return super.onOptionsItemSelected(item)
    }


}