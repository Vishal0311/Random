package com.acompworld.teamconnect.ui

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.SearchResponse
import com.acompworld.teamconnect.api.model.responses.*
import com.acompworld.teamconnect.repo.TeamConnectRepository
import com.acompworld.teamconnect.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val cm: ConnectivityManager,
    val repo: TeamConnectRepository
) : ViewModel() {
    private var initiallyCalledGallery = false
    private var initiallyCalledAllIncidence = false
    private var initiallyCalledFilteredIncidence = false
    var pendingQuery = MutableLiveData<String>()
    var currentYear = getCurrYear()
    private var initiallyCalledDepartmentList: Boolean = false
    private var initiallyCalledAboutUS = false
    var projectCode: String? = null
    var hasIntentConnection: Boolean = false
    private var initiallyCalledMyWall = false
    private var initiallyCalledDirectory = false
    private var initiallyCalledDownloads = false

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            hasIntentConnection = true
            //incase : app starts in offline mode  and then  on runtime net connects then this fetch data
            if (_myWall.value?.data == null && initiallyCalledMyWall) getMyWall()
            if (_directory.value?.data == null && initiallyCalledDirectory) getDirectory()
            if (_departments.value?.data == null && initiallyCalledDepartmentList) getDepartments()
            if (_about.value?.data == null && initiallyCalledAboutUS) getAboutUs()
            if (_gallery.value?.data == null && initiallyCalledGallery) getGallery()
            if (_allIncidence.value?.data == null && initiallyCalledAllIncidence) getAllIncidence()
            if (_filteredIncidence.value?.data == null && initiallyCalledFilteredIncidence) getFilteredIncidence()
            if (_download.value?.data == null && initiallyCalledDownloads) getDownload()

        }

        override fun onLost(network: Network) {
            super.onLost(network)
            hasIntentConnection = false
        }
    }

    var HOME_TOGGLE_BTN_ID: Int = R.id.tag_all
    var ABOUT_TOGGLE_BTN_ID: Int = R.id.tag_vision

    var searchingFun: (search: String) -> Job = { search ->
        search(search = search)
    }

    val readMore = MutableLiveData(true)
    private val builder = NetworkRequest.Builder()

    private val _myWall = MutableLiveData<Resource<MyWallResponse?>>()
    val myWall: LiveData<Resource<MyWallResponse?>> = _myWall

    private val _directory = MutableLiveData<Resource<DirectoryResponse?>>()
    var directory: LiveData<Resource<DirectoryResponse?>> = _directory

    private val _departments = MutableLiveData<Resource<DepartmentListResponse?>>()
    var departments: LiveData<Resource<DepartmentListResponse?>> = _departments

    private val _about = MutableLiveData<Resource<AboutRespone?>>()
    var about: LiveData<Resource<AboutRespone?>> = _about

    private val _gallery = MutableLiveData<Resource<GalleryResponse?>>()
    var gallery: LiveData<Resource<GalleryResponse?>> = _gallery

    private val _search = MutableLiveData<Resource<SearchResponse>>()
    var search: LiveData<Resource<SearchResponse>> = _search

    private val _allIncidence = MutableLiveData<Resource<IncidenceListResponse>>()
    var allIncidence: LiveData<Resource<IncidenceListResponse>> = _allIncidence

    private val _filteredIncidence = MutableLiveData<Resource<IncidenceListResponse>>()
    var filteredIncidence: LiveData<Resource<IncidenceListResponse>> = _filteredIncidence

    private val _download = MutableLiveData<Resource<DownloadResponse>>()
    var download: LiveData<Resource<DownloadResponse>> = _download


    init {
        runBlocking { cm.registerNetworkCallback(builder.build(), networkCallback) }
        getMyWall()
        getDirectory()
        getDepartments()
        getAboutUs()
        getGallery()
        getAllIncidence()
        getFilteredIncidence()
        getDownload()
    }





    fun getFilteredIncidence(
        projectCode: String = "wrihq",
        filter: String = "reportedbyme",
        userId: Int = 1
    ) = viewModelScope.launch {
        initiallyCalledFilteredIncidence = true
        this@MainViewModel.projectCode = projectCode
        _filteredIncidence.postValue(Resource.loading(_filteredIncidence.value?.data))
        try {
            if (hasIntentConnection) {
                val response = repo.getFilteredIncidence(projectCode, filter, userId)
                handleFilteredIncidenceResponse(response)
            } else {
                _filteredIncidence.postValue(
                    Resource.error(
                        data = _filteredIncidence.value?.data,
                        msg = "No Internet Connection...!"
                    )
                )
            }
        } catch (e: Exception) {
            Log.i("CAtch", "getFilteredIncidence: " + e.message)
            _filteredIncidence.postValue(
                Resource.error(
                    data = _filteredIncidence.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }

    }

    fun getAllIncidence(projectCode: String = "wrihq", filter: String = "all") =
        viewModelScope.launch {
            initiallyCalledAllIncidence = true
            this@MainViewModel.projectCode = projectCode
            _allIncidence.postValue(Resource.loading(_allIncidence.value?.data))
            try {
                if (hasIntentConnection) {
                    Log.i("MainViewModel", "getAllIncidence: inside if")
                    val response = repo.getAllIncidence(projectCode, filter)
                    handleAllIncidenceResponse(response)
                } else {
                    Log.i("MainViewModel", "getAllIncidence: inside else")
                    _allIncidence.postValue(
                        Resource.error(

                            data = _allIncidence.value?.data,
                            msg = "No Internet Connection...!"
                        )

                    )
                }
            } catch (e: Exception) {
                Log.i("CAtch", "getAllIncidence: " + e.message)
                _allIncidence.postValue(
                    Resource.error(
                        data = _allIncidence.value?.data,
                        msg = e.message ?: "Something went Wrong"
                    )
                )
            }
        }


    private fun getAboutUs(projectCode: String = "wrihq") = viewModelScope.launch {
        initiallyCalledAboutUS = true
        this@MainViewModel.projectCode = projectCode
        _about.postValue(Resource.loading(_about.value?.data))
        try {
            if (hasIntentConnection) {
                val respone = repo.getAboutUs(projectCode)
                handleAboutResource(respone)
            } else _about.postValue(
                Resource.error(
                    data = _about.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        } catch (e: Exception) {
            _about.postValue(
                Resource.error(
                    data = _about.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }


    fun getMyWall(projectCode: String = "wrihq") = viewModelScope.launch {
        initiallyCalledMyWall = true
        this@MainViewModel.projectCode = projectCode
        _myWall.postValue(Resource.loading(_myWall.value?.data))
        delay(100)
        try {
            if (hasIntentConnection) {
                val response = repo.getMyWall(projectCode)
                handleMyWallResponse(response)
            } else _myWall.postValue(
                Resource.error(
                    data = _myWall.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        } catch (e: Exception) {
            _myWall.postValue(
                Resource.error(
                    data = _myWall.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }

    fun getDownload(projectCode: String = "wrihq", search: String="") = viewModelScope.launch {
        initiallyCalledDownloads = true
        this@MainViewModel.projectCode = projectCode
        _download.postValue(Resource.loading(_download.value?.data))
        delay(100)
        try {
            Log.i("INSIDE", "getDownload: inside try block")
            if (hasIntentConnection) {
                Log.i("INSIDE", "getDownload: inside if block")
                val response = repo.getDownload(projectCode, search)
                Log.i("RES", "getDownload: $response")
                handleDownloadResponse(response)
            } else {
                Log.i("INSIDE", "getDownload: inside else block")
                _download.postValue(
                    Resource.error(
                        data = _download.value?.data,
                        msg = "No Internet Connection...!"
                    )
                )
            }
        } catch (e: Exception) {
            Log.i("INSIDE", "getDownload: inside catch block")
            _download.postValue(
                Resource.error(
                    data = _download.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
            Log.i("RES_data", "getDownload: "+_download.value?.data)
            Log.i("RES_msg", "getDownload: "+e.message)

        }
    }
    fun getDirectory(projectCode: String = "wrihq", search: String = "") = viewModelScope.launch {
        initiallyCalledDirectory = true
        this@MainViewModel.projectCode = projectCode
        _directory.postValue(Resource.loading(_directory.value?.data))
        try {
            if (hasIntentConnection) {
                val response = repo.getDirectory(projectCode, search)
                handleDirectoryResponse(response)
            } else _directory.postValue(
                Resource.error(
                    data = _directory.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        } catch (e: Exception) {
            _directory.postValue(
                Resource.error(
                    data = _directory.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }


    fun getDepartments(projectCode: String = "wrihq", search: String = "") = viewModelScope.launch {
        initiallyCalledDepartmentList = true
        this@MainViewModel.projectCode = projectCode
        _departments.postValue(Resource.loading(_departments.value?.data))
        try {
            if (hasIntentConnection) {
                val response = repo.getDepartments(projectCode, search)
                handleDepartmentResponse(response)
            } else _departments.postValue(
                Resource.error(
                    data = _departments.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        } catch (e: Exception) {
            _departments.postValue(
                Resource.error(
                    data = _departments.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }

    fun getCurrYear(): Int {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy")
        val year = formatter.format(date).toInt()
        return year
    }

    fun getGallery(projectCode: String = "wrihq", year: Int = currentYear) = viewModelScope.launch {
        currentYear = year
        initiallyCalledGallery = true
        this@MainViewModel.projectCode = projectCode
        _gallery.postValue(Resource.loading(_gallery.value?.data))
        try {
            if (hasIntentConnection) {
                val response = repo.getGallery(projectCode, year)
                handleGalleryResponse(response)
            } else {
                _gallery.postValue(
                    Resource.error(
                        data = _gallery.value?.data,
                        msg = "No Internet Connection...!"
                    )
                )
                if (currentYear > getCurrYear())
                    currentYear--
            }
        } catch (e: Exception) {
            _gallery.postValue(
                Resource.error(
                    data = _gallery.value?.data,
                    msg = "Something went Wrong"
                )
            )
            if (currentYear > getCurrYear())
                currentYear--
        }
    }


    fun search(projectCode: String = "wrihq", search: String? = null) = viewModelScope.launch {
        initiallyCalledDirectory = true
        this@MainViewModel.projectCode = projectCode
        _search.postValue(Resource.loading(_search.value?.data))
        try {
            if (hasIntentConnection) {
                val response = repo.search(projectCode, search)
                handleSearchResponse(response)
            } else _search.postValue(
                Resource.error(
                    data = _search.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        } catch (e: Exception) {
            _search.postValue(
                Resource.error(
                    data = _search.value?.data,
                    msg = "Something went Wrong"
                )
            )
        }
    }

    private fun handleSearchResponse(response: Response<SearchResponse>) {
        if (response.isSuccessful) {
            _search.postValue(Resource.success(response.body()!!))
        } else _search.postValue(
            Resource.error(
                data = _search.value?.data,
                msg = "Data not found...!"
            )
        )
    }

    private fun handleGalleryResponse(response: Response<GalleryResponse>) {
        if (response.isSuccessful) {
            _gallery.postValue(Resource.success(response.body()!!))
        } else {
            _gallery.postValue(
                Resource.error(
                    msg = "No Data found!"
                )
            )
            if (currentYear > getCurrYear())
                currentYear--
        }
    }

    //TODO : Make its Code D.R.Y
    private fun handleDepartmentResponse(response: Response<DepartmentListResponse>) {
        if (response.isSuccessful) {
            _departments.postValue(Resource.success(response.body()!!))
        } else _departments.postValue(
            Resource.error(
                data = _departments.value?.data,
                msg = response.message() ?: "Error ${response.code()} found...!"
            )
        )
    }

    private fun handleDirectoryResponse(response: Response<DirectoryResponse>) {
        if (response.isSuccessful) {
            _directory.postValue(Resource.success(response.body()!!))
        } else _directory.postValue(
            Resource.error(
                data = _directory.value?.data,
                msg = response.message() ?: "Error ${response.code()} found...!"
            )
        )
    }




    private fun handleAboutResource(response: Response<AboutRespone>) {
        if (response.isSuccessful)
            _about.postValue(Resource.success(response.body()))
        else _about.postValue(
            Resource.error(
                data = _about.value?.data,
                msg = response.message() ?: "Error ${response.code()} found...!"
            )
        )
    }

    private fun handleMyWallResponse(response: Response<MyWallResponse>) {
        if (response.isSuccessful)
            _myWall.postValue(Resource.success(response.body()!!))
        else _myWall.postValue(
            Resource.error(
                data = _myWall.value?.data,
                msg = response.message() ?: "Error ${response.code()} found...!"
            )
        )
    }

    private fun handleAllIncidenceResponse(response: Response<IncidenceListResponse>) {
        if (response.isSuccessful) {
            Log.i("MainViewModel", "handleAllIncidenceResponse: " + response.body()?.data)
            _allIncidence.postValue(Resource.success(response.body()!!))
        } else _allIncidence.postValue(
            Resource.error(
                data = _allIncidence.value?.data,
                msg = response.message() ?: "Error ${response.code()} found...!"
            )
        )
    }

    private fun handleFilteredIncidenceResponse(response: Response<IncidenceListResponse>) {
        if (response.isSuccessful) {
            Log.i("MainViewModel", "handleAllIncidenceResponse: " + response.body()?.data)
            _filteredIncidence.postValue(Resource.success(response.body()!!))
        } else _filteredIncidence.postValue(
            Resource.error(
                data = _filteredIncidence.value?.data,
                msg = response.message() ?: "Error ${response.code()} found...!"
            )
        )
    }

    private fun handleDownloadResponse(response: Response<DownloadResponse>) {

        Log.i("response", "handleDownloadResponse: "+response.body()?.message)

        if (response.isSuccessful)
            _download.postValue(Resource.success(response.body()!!))
        else _download.postValue(
            Resource.error(
                data = _download.value?.data,
                msg = response.message() ?: "Error ${response.code()} found...!"
            )
        )

    }


    fun visited() {
        _gallery.postValue(Resource.error(msg = null))
    }
}
//TODO projCode