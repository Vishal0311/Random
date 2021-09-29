package com.acompworld.teamconnect.ui.fragments.home

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acompworld.teamconnect.api.model.responses.InfoListResponse
import com.acompworld.teamconnect.api.model.responses.InfoResponse
import com.acompworld.teamconnect.repo.TeamConnectRepository
import com.acompworld.teamconnect.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private  val cm: ConnectivityManager,
  private  val repo: TeamConnectRepository
) : ViewModel() {
    private var initiallyCalledinfo = false
    private var initiallyCalledinfoList = false
    private var hasIntentConnection: Boolean = false
    private val builder = NetworkRequest.Builder()
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
    runBlocking { cm.registerNetworkCallback(builder.build(), networkCallback) }

}

   private  val _infoList = MutableLiveData<Resource<InfoListResponse>>()
    var infoList :LiveData<Resource<InfoListResponse>> = _infoList

    private val _info = MutableLiveData<Resource<InfoResponse>>()
            var info  :LiveData<Resource<InfoResponse>> = _info

     fun getinfoList(projectCode: String = "wrihq", type : Section) = viewModelScope.launch {
        initiallyCalledinfoList = true
        _infoList.postValue(Resource.loading(_infoList.value?.data))
         delay(100)
        try {
            if (hasIntentConnection) {
                val response = repo.getInfoList(projectCode, type)
                handleInfoListResponse(response)
            } else {
                _infoList.postValue(
                    Resource.error(
                        data = _infoList.value?.data,
                        msg = "No Internet Connection...!"
                    )
                )
            }
        } catch (e: Exception) {
            _infoList.postValue(
                Resource.error(
                    data = _infoList.value?.data,
                    msg =  "Something went Wrong"
                )
            )
        }
    }

    fun getinfo(projectCode: String = "wrihq",section: Section, rid:Int) = viewModelScope.launch {
        initiallyCalledinfo = true
        _info.postValue(Resource.loading(_info.value?.data))
        delay(100)
        try {
            if (hasIntentConnection) {
                val response = repo.getInfo(projectCode,section, rid)
                handleInfoResponse(response)
            } else {
                _info.postValue(
                    Resource.error(
                        data = _info.value?.data,
                        msg = "No Internet Connection...!"
                    )
                )
            }
        } catch (e: Exception) {
            _info.postValue(
                Resource.error(
                    data = _info.value?.data,
                    msg =  "Something went Wrong"
                )
            )
        }
    }

    private fun handleInfoResponse(response: Response<InfoResponse>) {
        if (response.isSuccessful) {
            _info.postValue(Resource.success(response.body()!!))
        } else {
            _info.postValue(
                Resource.error(
                    data = _info.value?.data,
                    msg = "Data not found...!"
                )
            )
            Log.d("omega_infoList",response.errorBody().toString())
        }
    }

    private fun handleInfoListResponse(response: Response<InfoListResponse>) {
        if (response.isSuccessful) {
            _infoList.postValue(Resource.success(response.body()!!))
        } else {
            _infoList.postValue(
                Resource.error(
                    data = _infoList.value?.data,
                    msg = "Data not found...!"
                )
            )
            Log.d("omega_infoList",response.errorBody().toString())
        }
    }
}