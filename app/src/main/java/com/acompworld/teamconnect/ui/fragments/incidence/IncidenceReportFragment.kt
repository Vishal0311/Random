package com.acompworld.teamconnect.ui.fragments.incidence

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.entities.Data
import com.acompworld.teamconnect.api.model.requests.Photo
import com.acompworld.teamconnect.api.model.responses.IncidenceReportResponse
import com.acompworld.teamconnect.databinding.FragmentIncidenceReportBinding
import com.acompworld.teamconnect.ui.MainViewModel
import com.acompworld.teamconnect.utils.GenericListAdapter
import com.acompworld.teamconnect.utils.Resource
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class IncidenceReportFragment : Fragment() {

    private val viewModel: IncidenceViewModel by viewModels()
    private var binding: FragmentIncidenceReportBinding? = null
    private var myCal = Calendar.getInstance()


    var selectedDate: String = ""
    var selectedTime: String = ""
    val photos: List<Photo>? = null


    val REQUEST_EXTERNAL_STORAGE = 100
    var uriList: List<Uri>? = null
    var mediaPath: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.incidence_reported_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
        val types = resources.getStringArray(R.array.types)
        val typeAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, types)
        binding?.typeDropdown?.setAdapter(typeAdapter)

        binding?.typeDropdown?.setOnDismissListener {
            binding?.typeDropdown?.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_dropdown,
                0
            )
        }
        binding?.typeDropdown?.setOnClickListener {
            binding?.typeDropdown?.showDropDown()
            binding?.typeDropdown?.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_dropup,
                0
            )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIncidenceReportBinding.inflate(inflater, container, false)
        binding?.typeDropdown?.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_dropdown,
            0
        )


        binding?.addImage?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireView().context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )

                    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    startActivityForResult(galleryIntent, 0)

                } else {
                    ActivityCompat.requestPermissions(
                        requireView().context as Activity,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_EXTERNAL_STORAGE
                    )

                }
            }
        }


        // date picker
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCal.set(Calendar.YEAR, year)
            myCal.set(Calendar.MONTH, month)
            myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCal)
        }
        binding?.date?.setOnClickListener {
            DatePickerDialog(
                requireView().context,
                datePicker,
                myCal.get(Calendar.YEAR),
                myCal.get(Calendar.MONTH),
                myCal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }

        // time picker
        binding?.time?.setOnClickListener {
            openTimePicker()
        }

        Log.i("SelectedDate", "onCreateView: $selectedDate")
        if (selectedDate.isNotEmpty()) {
            binding!!.date.text = selectedDate
        }

        binding?.apply {
            submitReport.setOnClickListener {
                val time: String = time.text.toString()
                val type: String = typeDropdown.text.toString()
                val date: String = date.text.toString()
                val area: String = area.text.toString()
                val detail: String = detail.text.toString()
                val title: String = title.text.toString()

                addReport(time, type, date, area, detail, title, "WRIHQ", "1")
                viewModel.incidenceReport.observe({ lifecycle }) {
                    handleResource(it)
                }

            }
        }



        return binding?.root
    }

    private fun handleResource(res: Resource<IncidenceReportResponse?>?) {
        Log.i("Resource", "handleResource: ${res?.msg}")
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true
            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                Toast.makeText(
                    requireView().context,
                    res.msg ?: "Something went wrong",
                    Toast.LENGTH_SHORT
                )
                    .show()
                val msg = res.data?.status
                Log.i("ERROR_", res.msg ?: "")
                Log.i("ERROR_", "${res.data}")
            }
            is Resource.success -> {
                binding?.progressCircular?.isVisible = false
                Toast.makeText(
                    requireView().context,
                    "Incidence Successfully Added",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun addReport(
        time: String,
        type: String,
        date: String,
        area: String,
        detail: String,
        title: String,
        projectCode: String,
        userid: String,
    ) {
        try {

            Log.i("saveChanges", "saveChanges: ")
            viewModel.incidenceReport(
                type,
                title,
                date,
                time,
                area,
                detail,
                projectCode,
                userid,
                photos
            )


        } catch (e: Exception) {
            Toast.makeText(requireView().context, e.message, Toast.LENGTH_SHORT).show()
        }

    }


    // update calender text
    private fun updateLabel(myCal: Calendar) {
        val format = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(format, Locale.UK)
        selectedDate = sdf.format(myCal.time)
        binding!!.date.text = selectedDate
        Log.i("SelectedDate", "onCreateView: $selectedDate")

    }

    // update time text
    private fun openTimePicker() {
        val isSystem24HrFormat = android.text.format.DateFormat.is24HourFormat(context)
        val clockFormat = if (isSystem24HrFormat) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Time of incidence")
            .build()

        picker.show(childFragmentManager, "TAG")
        picker.addOnPositiveButtonClickListener {
            var h = picker.hour.toString()
            var m = picker.minute.toString()

            // if hour or min is in single digit convert it to double digit
            if (h.length == 1) h = "0$h"
            if (m.length == 1) m = "0$m"


            selectedTime = "$h:$m"
            binding!!.time.text = selectedTime
            Log.i("selectedTime", "onCreateView: $selectedTime")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == Activity.RESULT_OK && null != data) {

                // Get the Image from data
                var selectedImage: Uri?
                val files: MutableList<File> = ArrayList()
                var file: File
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                if (data.clipData != null) { // CHECKING FOR MULTIPLE FILES SELECTED OR NOT
                    for (i in 0 until data.clipData!!.itemCount) {
                        selectedImage = data.clipData!!.getItemAt(i).uri
                        val cursor = requireContext().contentResolver.query(
                            selectedImage,
                            filePathColumn,
                            null,
                            null,
                            null
                        )!!
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        mediaPath = cursor.getString(columnIndex)
                        /*AddAssetRequestphotos photopath = new AddAssetRequestphotos();
                photopath.setPath(mediaPath);
                photoList.add(photopath);*/file = File(mediaPath)
                        files.add(file)
                        cursor.close()
                    }
                } else {
                    selectedImage = data.data
                    val cursor = requireContext().contentResolver.query(
                        selectedImage!!,
                        filePathColumn,
                        null,
                        null,
                        null
                    )!!
                    cursor.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
                    /*AddAssetRequestphotos photopath = new AddAssetRequestphotos();
                photopath.setPath(mediaPath);
                photoList.add(photopath);*/file = File(mediaPath)
                    files.add(file)
                    cursor.close()
                }
                //   uploadFile(files)
                setUpRV(files)
            } else {
                Toast.makeText(context, "You haven't picked Image/Video", Toast.LENGTH_LONG).show()
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpRV(files: MutableList<File>) {
        binding?.apply {
            if (imageInGrid.adapter == null){
                imageInGrid.layoutManager = GridLayoutManager(requireContext(), 2)
               // imageInGrid.adapter = getAdapter(files)
            }
            else{
               // imageInGrid.adapter as GenericListAdapter<Data>).submitList(files)
            }

        }

    }


}