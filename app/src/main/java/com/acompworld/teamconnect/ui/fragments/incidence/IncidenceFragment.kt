package com.acompworld.teamconnect.ui.fragments.incidence

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.entities.Data
import com.acompworld.teamconnect.api.model.responses.IncidenceListResponse
import com.acompworld.teamconnect.databinding.FragmentIncidenceBinding
import com.acompworld.teamconnect.ui.MainViewModel
import com.acompworld.teamconnect.utils.Constants
import com.acompworld.teamconnect.utils.GenericListAdapter
import com.acompworld.teamconnect.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncidenceFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var binding: FragmentIncidenceBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIncidenceBinding.inflate(inflater, container, false)

        binding?.navIncidenceReport?.setOnClickListener{
            findNavController().navigate(R.id.action_nav_incidence_to_nav_incidenceReport)
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.allIncidence.observe({ lifecycle }) { handleResourcesForAll(view, it) }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.incidence_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id  =item.getItemId()

        if(id==R.id.incidence_menu_all){
            viewModel.allIncidence.observe({ lifecycle }) { view?.let { it1 -> handleResourcesForAll(it1, it) } }
        }
        else if(id == R.id.incidence_menu_reported){
            viewModel.filteredIncidence.observe({lifecycle}) {view?.let { it1 -> handleResourcesForFiltered(it1, it) }}
        }


        return super.onOptionsItemSelected(item)
    }

    private fun handleResourcesForFiltered(view: View, res: Resource<IncidenceListResponse>?) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true
            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getFilteredIncidence("wrihq","reportedbyme",1)
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }


    private fun handleResourcesForAll(view: View, res: Resource<IncidenceListResponse>) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true
            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getAllIncidence("wrihq","all")
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(it: IncidenceListResponse) {
        binding?.apply {
            if (incidenceRv.adapter == null) {
                incidenceRv.layoutManager = LinearLayoutManager(requireContext())
                incidenceRv.adapter = getAdapter(it.data)
            } else (incidenceRv.adapter as GenericListAdapter<Data>).submitList(it.data)


        }
    }

    private fun getAdapter(list: List<Data>) =
        object : GenericListAdapter<Data>(
            layoutId = R.layout.item_incidence_view,
            bind = { item, holder, itemCount ->
                val itemBinding =
                    com.acompworld.teamconnect.databinding.ItemIncidenceViewBinding.bind(holder.itemView)
                itemBinding.apply {
                    tvIncidencePlace.text = item.Area
                    tvIncidenceTitle.text = item.IncidenceTitle

                    val place: kotlin.String =item.date
                    val time: kotlin.String = item.Time
                    val final= "$place | $time"

                    tvIncidenceDate.text = final

                    root.setOnClickListener{
                        findNavController().navigate(R.id.action_nav_incidenceDetail_to_incidenceDetailsFragment,
                            bundleOf().also {
                                it.putString(Constants.PROJECT_Code, viewModel.projectCode)
                                it.putInt("incidenceId",item.IncidenceID)
                            }
                        )
                    }

                }
            }
        ) {}.apply {
            submitList(list)
        }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}