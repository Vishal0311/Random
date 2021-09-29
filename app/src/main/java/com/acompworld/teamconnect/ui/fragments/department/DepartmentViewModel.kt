package com.acompworld.teamconnect.ui.fragments.department

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acompworld.teamconnect.api.model.responses.DepartmentDocumentsResponse
import com.acompworld.teamconnect.api.model.responses.DepartmentInfoResponse
import com.acompworld.teamconnect.repo.TeamConnectRepository
import com.acompworld.teamconnect.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DepartmentViewModel @Inject constructor(
    val cm: ConnectivityManager,
    val repo: TeamConnectRepository
) : ViewModel(){

    private var hasIntentConnection: Boolean = false
    private val builder = NetworkRequest.Builder()
    private var initiallyCalledDeptDocs = false
    private var initiallyCalledDept: Boolean= false
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
             hasIntentConnection = true
        }
        override fun onLost(network: Network) {
            super.onLost(network)
            hasIntentConnection = false
        }
    }

    init {
        runBlocking{ cm.registerNetworkCallback(builder.build(), networkCallback) }
        Log.d("omega","depatment viewmodel  created")
    }

    private val _departmentIfo = MutableLiveData<Resource<DepartmentInfoResponse?>>()
    val departmentInfo: LiveData<Resource<DepartmentInfoResponse?>> = _departmentIfo

    private val _deptDocs = MutableLiveData<Resource<DepartmentDocumentsResponse>>()
            val deptDocs : LiveData<Resource<DepartmentDocumentsResponse>> = _deptDocs


    fun getDepartmentDocsByRid(projectCode: String, deptId: Int, rId : Int) = viewModelScope.launch {
        Log.d("omega_doc", "ProjCode = $projectCode deptId = $deptId  rId = $rId ")
        initiallyCalledDeptDocs = true
        _departmentIfo.postValue(Resource.loading(_departmentIfo.value?.data))
        delay(150)
        try {
            if (hasIntentConnection){
                val respone = repo.getDocs(projectCode,deptId, rId)
                handleDepartmentDocsResource(respone)
            } else _deptDocs.postValue(
                Resource.error(
                    data = _deptDocs.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        }catch (e: Exception) {
            _deptDocs.postValue(
                Resource.error(
                    data = _deptDocs.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }

    private fun handleDepartmentDocsResource(response: Response<DepartmentDocumentsResponse>) {
        if (response.isSuccessful) {
            _deptDocs.postValue(Resource.success(response.body()!!))
        }
        else {
            _deptDocs.postValue(
                Resource.error(
                    data = _deptDocs.value?.data,
                    msg = response.message() ?: "Error ${response.code()} found...!"
                )
            )
        }
    }


    fun getDepartmentByID(projectCode: String, deptID: Int) = viewModelScope.launch {
        initiallyCalledDept = true
        _departmentIfo.postValue(Resource.loading(_departmentIfo.value?.data))
        try {
            delay(150)
            if (hasIntentConnection){
                val respone = repo.getDepartmentInfo(projectCode,deptID)
                handleDepartmentResource(respone)
            } else _departmentIfo.postValue(
                Resource.error(
                    data = _departmentIfo.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        }catch (e: Exception) {
            _departmentIfo.postValue(
                Resource.error(
                    data = _departmentIfo.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }

    private fun handleDepartmentResource(response: Response<DepartmentInfoResponse>) {
        if (response.isSuccessful)
            _departmentIfo.postValue(Resource.success(response.body()))
        else _departmentIfo.postValue(
            Resource.error(
                data = _departmentIfo.value?.data,
                msg = response.message()?:  "Error ${response.code()} found...!"
            )
        )
    }
}