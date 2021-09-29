package com.acompworld.teamconnect.ui.fragments.gallery

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acompworld.teamconnect.api.model.responses.PhotosReponse
import com.acompworld.teamconnect.repo.TeamConnectRepository
import com.acompworld.teamconnect.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    val cm: ConnectivityManager,
    val repo: TeamConnectRepository
) : ViewModel() {
    private var initiallyCalledPhotos = false
    private val builder = NetworkRequest.Builder()
    private var hasIntentConnection: Boolean = false
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

    private val _photos = MutableLiveData<Resource<PhotosReponse>>()
    var photos  : LiveData<Resource<PhotosReponse>> = _photos

    fun getPhotos(projectCode: String="wrihq", ablumID: Int) = viewModelScope.launch {
        initiallyCalledPhotos=true
        delay(100)
        _photos.postValue(Resource.loading(_photos.value?.data))
        try{
            delay(150)
            if (hasIntentConnection){
                val respone = repo.getPhotos(projectCode,albumID = ablumID)
                handleResources(respone)
            } else _photos.postValue(
                Resource.error(
                    data = _photos.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        }catch (e: Exception) {
            _photos.postValue(
                Resource.error(
                    data = _photos.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }

    private fun handleResources(response: Response<PhotosReponse>) {
        if (response.isSuccessful)
            _photos.postValue(Resource.success(response.body()!!))
        else _photos.postValue(
            Resource.error(
                data = _photos.value?.data,
                msg = response.message()?:  "Error ${response.code()} found...!"
            )
        )
    }
}