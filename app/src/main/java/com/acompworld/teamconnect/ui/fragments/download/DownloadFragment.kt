package com.acompworld.teamconnect.ui.fragments.download

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acompworld.teamconnect.api.model.responses.DownloadResponse
import com.acompworld.teamconnect.databinding.FragmentDownloadBinding
import com.acompworld.teamconnect.ui.MainViewModel
import com.acompworld.teamconnect.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class DownloadFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var binding:FragmentDownloadBinding?=null
    var models: ArrayList<DownloadModel> = ArrayList<DownloadModel>()
    var objects= mutableListOf<Any>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDownloadBinding.inflate(inflater,container,false)

//        val model1 = DownloadModel("Category 1", "Title 1", "20mb")
//        val model2 = DownloadModel("Category 1", "Title 1", "20mb")
//        val model3 = DownloadModel("Category 1", "Title 1", "20mb")
//        val model4 = DownloadModel("Category 1", "Title 1", "20mb")
//
//        models.add(model1)
//        models.add(model2)
//        models.add(model3)
//        models.add(model4)
//
//        for (fake in models) {
//            if (objects.indexOf(fake.category) == -1) {
//                objects.add(fake.category)
//                objects.add(fake)
//            } else {
//                objects.add(fake)
//            }
//        }
//
//        Log.i("OBJECTS_LIST", "onCreateView: $objects")



        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.download.observe({ lifecycle }) {handleResourse(view,it)}
    }

    private fun handleResourse(view: View, res: Resource<DownloadResponse>?) {
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true
            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getDownload("wrihq","")
                    }
                }.show()
            }
            is Resource.success -> {
                setUpViews(res.data!!)
                binding?.progressCircular?.isVisible = false
            }
        }
    }

    private fun setUpViews(data: DownloadResponse) {

        var i=0
        var j=0
        while(i<data.data.size){
            while(j< data.data[i].Downloads.size){
                val model = DownloadModel(data.data[i].category, data.data[i].Downloads[j].title, "")
                models.add(model)
                j++
            }
            i++
        }

        for (fake in models) {
            Log.i("FAKE", "setUpViews: "+fake)
            if (objects.indexOf(fake.category) == -1) {
                objects.add(fake.category)
                objects.add(fake)
            } else {
                objects.add(fake)
            }
        }

        Log.i("OBJECTS_LIST", "onCreateView: $objects")

        binding?.apply {

            val adapter= DownloadAdapter(objects)
            adapter.setItem(objects)
            categoryRv.layoutManager = LinearLayoutManager(requireContext())
            categoryRv.adapter=DownloadAdapter(objects)
        }
    }


}