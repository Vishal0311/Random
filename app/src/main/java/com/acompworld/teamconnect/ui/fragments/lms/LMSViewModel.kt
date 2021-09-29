package com.acompworld.teamconnect.ui.fragments.lms

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acompworld.teamconnect.api.model.responses.LMSResponse
import com.acompworld.teamconnect.repo.TeamConnectRepository
import com.acompworld.teamconnect.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LMSViewModel @Inject constructor(
    val cm: ConnectivityManager,
    val repo: TeamConnectRepository
) : ViewModel(){
    private val builder = NetworkRequest.Builder()
    private var hasIntentConnection= false
    private var initiallyCalledLMS = false
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
    cm.registerNetworkCallback(builder.build(), networkCallback)
}

    private val _lmsResponse = MutableLiveData<Resource<LMSResponse?>>()
    val lms: LiveData<Resource<LMSResponse?>> = _lmsResponse


    fun getLMS(projectCode: String, userId: Int) = viewModelScope.launch {
        initiallyCalledLMS=true
        _lmsResponse.postValue(Resource.loading(_lmsResponse.value?.data))
       try{
           delay(150)
           if (hasIntentConnection){
                val respone = repo.getLMS(projectCode,userId)
                handleLMSResource(respone)
            } else _lmsResponse.postValue(
                Resource.error(
                    data = _lmsResponse.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        }catch (e: Exception) {
            _lmsResponse.postValue(
                Resource.error(
                    data = _lmsResponse.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }

    private fun handleLMSResource(response: Response<LMSResponse>) {
        if (response.isSuccessful)
            _lmsResponse.postValue(Resource.success(response.body()))
        else _lmsResponse.postValue(
            Resource.error(
                data = _lmsResponse.value?.data,
                msg = response.message()?:  "Error ${response.code()} found...!"
            )
        )
    }
}

