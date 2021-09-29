package com.acompworld.teamconnect.ui.fragments.directory

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acompworld.teamconnect.api.model.responses.ContactResponse
import com.acompworld.teamconnect.api.model.responses.EmployeeResponse
import com.acompworld.teamconnect.repo.TeamConnectRepository
import com.acompworld.teamconnect.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DirectoryViewModel @Inject constructor(
    val cm: ConnectivityManager,
    val repo: TeamConnectRepository
) : ViewModel(){
    private val builder = NetworkRequest.Builder()
    private var hasIntentConnection= false
    private var initiallyCalledEmp = false
    private var initiallyCalledContact = false
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

    private val _employeeResponse = MutableLiveData<Resource<EmployeeResponse?>>()
    val employee: LiveData<Resource<EmployeeResponse?>> = _employeeResponse

    private val _contactResponse = MutableLiveData<Resource<ContactResponse?>>()
    val contact: LiveData<Resource<ContactResponse?>> = _contactResponse



    fun getEmployeeByID(projectCode: String, empID: String) = viewModelScope.launch {
        initiallyCalledEmp=true
        _employeeResponse.postValue(Resource.loading(_employeeResponse.value?.data))
       try{
           delay(150)
           if (hasIntentConnection){
                val respone = repo.getEmployeeByID(projectCode,empID)
                handleEmployeeResource(respone)
            } else _employeeResponse.postValue(
                Resource.error(
                    data = _employeeResponse.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        }catch (e: Exception) {
            _employeeResponse.postValue(
                Resource.error(
                    data = _employeeResponse.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }

    private fun handleEmployeeResource(response: Response<EmployeeResponse>) {
        if (response.isSuccessful)
            _employeeResponse.postValue(Resource.success(response.body()))
        else _employeeResponse.postValue(
            Resource.error(
                data = _employeeResponse.value?.data,
                msg = response.message()?:  "Error ${response.code()} found...!"
            )
        )
    }

    fun getContactByID(projectCode: String, contactID: String) = viewModelScope.launch {
        initiallyCalledContact=true
        _contactResponse.postValue(Resource.loading(_contactResponse.value?.data))
        delay(100)
        try {
            if (hasIntentConnection){
                val response = repo.getContactByID(projectCode,contactID)
                handleContactResource(response)
            } else _contactResponse.postValue(
                Resource.error(
                    data = _contactResponse.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        }catch (e: Exception) {
            _contactResponse.postValue(
                Resource.error(
                    data = _contactResponse.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }

    private fun handleContactResource(response: Response<ContactResponse>) {
        if (response.isSuccessful)
            _contactResponse.postValue(Resource.success(response.body()))
        else _contactResponse.postValue(
            Resource.error(
                data = _contactResponse.value?.data,
                msg = response.message()?:  "Error ${response.code()} found...!"
            )
        )
    }

    //TODO : Unregister networkCallBack
}

