package com.acompworld.teamconnect.ui.fragments.incidence

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.responses.IncidenceEditResponse
import com.acompworld.teamconnect.databinding.FragmentIncidenceEditBinding
import com.acompworld.teamconnect.utils.Resource
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class IncidenceEditFragment : Fragment() {

    private val viewModel: IncidenceViewModel by viewModels()
    private var binding: FragmentIncidenceEditBinding? = null
    private var myCal = Calendar.getInstance()


    var id_bundle: Int? = null
    var title_bundle: String = ""
    var date_bundle: String = ""
    var time_bundle: String = ""
    var area_bundle: String = ""
    var type_bundle: String = ""
    var detail_bundle: String = ""

    var selectedDate: String = ""
    var selectedTime: String = ""

    val REQUEST_EXTERNAL_STORAGE = 100
    var uriList: List<Uri>? = null
    var mediaPath:String?=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            id_bundle = getInt("id")
            title_bundle = getString("title").toString()
            date_bundle = getString("date").toString()
            time_bundle = getString("time").toString()
            area_bundle = getString("area").toString()
            type_bundle = getString("type").toString()
            detail_bundle = getString("detail").toString()
        }
        if (time_bundle.isNotEmpty()) {
            var temp = time_bundle
            //11:53PM
            val displayFormat = SimpleDateFormat("HH:mm")
            val parseFormat = SimpleDateFormat("hh:mma")
            val date = parseFormat.parse(temp)
            time_bundle = displayFormat.format(date)
            Log.i("24HR", "onCreate: $time_bundle")
        }

        Log.i("bundle", "onCreate: $id_bundle $title_bundle")

    }

    override fun onResume() {
        super.onResume()
        val types=resources.getStringArray(R.array.types)
        val typeAdapter= ArrayAdapter(requireContext(), R.layout.dropdown_item,types)
        binding?.typeDropdown?.setAdapter(typeAdapter)

        binding?.typeDropdown?.setOnDismissListener {
            binding?.typeDropdown?.setCompoundDrawablesWithIntrinsicBounds(0,0,
                R.drawable.ic_dropdown,0)
        }
        binding?.typeDropdown?.setOnClickListener {
            binding?.typeDropdown?.showDropDown()
            binding?.typeDropdown?.setCompoundDrawablesWithIntrinsicBounds(0,0,
                R.drawable.ic_dropup,0)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIncidenceEditBinding.inflate(inflater, container, false)
        binding?.typeDropdown?.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_dropdown,0)



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
        initView()
        Log.i("SelectedDate", "onCreateView: $selectedDate")
        if (selectedDate.isNotEmpty()) {
            binding!!.date.text = selectedDate
        }




        binding?.apply {

            saveEdit.setOnClickListener {

                val time: String = binding?.time?.text.toString()
                val type: String = binding?.typeDropdown?.text.toString()
                val date: String = binding?.date?.text.toString()
                val area: String = binding?.area?.text.toString()
                val detail: String = binding?.detail?.text.toString()
                Log.i(
                    "EditFragment", "onCreateView: time=" + time + " type=" + type + " date="
                            + date + " area=" + area + " detail=" + detail +" id="+id_bundle+"" +
                            "title= "+title_bundle
                )


                var flag = 0
                if (time.isEmpty()) {
                    flag = 1
                }
                if (date.isEmpty()) {
                    flag = 1
                }

                if (flag != 1) {
                    Log.i(
                        "EditFragment", "onCreateView: time=" + time + " type=" + type + " date="
                                + date + " area=" + area + " detail=" + detail +" id="+id_bundle+"" +
                                "title= "+title_bundle
                    )
                    saveChanges(
                        time, type,
                        date,
                        area,
                        detail
                    )
                    viewModel.incidenceEdit.observe({ lifecycle }) {
                        handleResource(it)
                    }
                } else {
                    Toast.makeText(requireView().context, "Provide time and date", Toast.LENGTH_SHORT).show()
                    Log.i(
                        "EditFragment", "onCreateView: time=" + time + " type=" + type + " date="
                                + date + " area=" + area + " detail=" + detail +" id="+id_bundle+"" +
                                "title= "+title_bundle
                    )
                }

            }
        }





        return binding?.root
    }


    private fun handleResource(res: Resource<IncidenceEditResponse?>?) {
        Log.i("Resource", "handleResource: ${res?.msg}")
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true
            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                Toast.makeText(requireView().context, res.msg?:" :Something went wrong ", Toast.LENGTH_SHORT)
                    .show()
                val msg = res.data?.status
                Log.i("ERROR", "handleResource: " + msg)
            }
            is Resource.success -> {
                binding?.progressCircular?.isVisible = false
                Toast.makeText(
                    requireView().context,
                    "Incidence Successfully Updated",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun initView() {
        binding?.apply {
            area.text = Editable.Factory.getInstance().newEditable(area_bundle)

            if (selectedTime.isNotEmpty())
                time.text = selectedTime
            else
                time.text = Editable.Factory.getInstance().newEditable(time_bundle)
            if (selectedDate.isNotEmpty()) {
                date.text = selectedDate
            } else
                date.text = Editable.Factory.getInstance().newEditable(date_bundle)

            typeDropdown.text = Editable.Factory.getInstance().newEditable(type_bundle)
            detail.text = Editable.Factory.getInstance().newEditable(detail_bundle)

        }
    }

    private fun saveChanges(
        time: String,
        type: String,
        date: String,
        area: String,
        detail: String
    ) {
        try {
            Log.i("saveChanges", "saveChanges: ")
            viewModel.incidenceEdit(id_bundle, title_bundle, date, time, area, type, detail)

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
            if(h.length==1) h="0$h"
            if(m.length==1) m="0$m"


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
            } else {
                Toast.makeText(context, "You haven't picked Image/Video", Toast.LENGTH_LONG).show()
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }


}