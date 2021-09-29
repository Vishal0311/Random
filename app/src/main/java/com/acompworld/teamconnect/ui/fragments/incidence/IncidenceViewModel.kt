package com.acompworld.teamconnect.ui.fragments.incidence

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acompworld.teamconnect.api.model.requests.IncidenceEditRequest
import com.acompworld.teamconnect.api.model.requests.IncidenceReportRequest
import com.acompworld.teamconnect.api.model.requests.Photo
import com.acompworld.teamconnect.api.model.responses.IncidenceDetailResponse
import com.acompworld.teamconnect.api.model.responses.IncidenceEditResponse
import com.acompworld.teamconnect.api.model.responses.IncidenceReportResponse
import com.acompworld.teamconnect.repo.TeamConnectRepository
import com.acompworld.teamconnect.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class IncidenceViewModel @Inject constructor(
    val cm: ConnectivityManager,
    val repo: TeamConnectRepository
) : ViewModel() {
    private var hasIntentConnection: Boolean = false
    private val builder = NetworkRequest.Builder()
    private var initiallyCalledDetails: Boolean = false
    private var initiallyCalledEdit: Boolean = false
    private var initiallyCalledReport: Boolean = false
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

    private val _incidenceDetail = MutableLiveData<Resource<IncidenceDetailResponse?>>()
    val incidenceDetail: LiveData<Resource<IncidenceDetailResponse?>> = _incidenceDetail

    fun getIncidenceDetailById(projectCode: String, incidenceID: Int) = viewModelScope.launch {
        initiallyCalledDetails = true
        _incidenceDetail.postValue(Resource.loading(_incidenceDetail.value?.data))
        try {
            delay(150)
            // CHANGE EVERY RED HIGHLIGHTS
            if (hasIntentConnection) {
                val response = repo.getIncidenceDetail(projectCode, incidenceID)
                handleIncidenceResource(response)
            } else _incidenceDetail.postValue(
                Resource.error(
                    data = _incidenceDetail.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        } catch (e: Exception) {
            _incidenceDetail.postValue(
                Resource.error(
                    data = _incidenceDetail.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }

    private fun handleIncidenceResource(response: Response<IncidenceDetailResponse>) {
        if (response.isSuccessful)
            _incidenceDetail.postValue(Resource.success(response.body()))
        else _incidenceDetail.postValue(
            Resource.error(
                data = _incidenceDetail.value?.data,
                msg = response.message() ?: "Error ${response.code()} found...!"
            )
        )
    }


    private val _incidenceEdit = MutableLiveData<Resource<IncidenceEditResponse?>>()
    var incidenceEdit: LiveData<Resource<IncidenceEditResponse?>> = _incidenceEdit

    fun incidenceEdit(
        incidenceID: Int? =null, incidenceTitle: String="", incidenceDate: String="",
        incidenceTime: String="", incidenceArea: String="", incidenceType: String="",
        incidenceDetail: String=""
    ) = viewModelScope.launch {
        initiallyCalledEdit = true
        delay(100)
        _incidenceEdit.postValue(Resource.loading(_incidenceEdit.value?.data))
        try {
            delay(100)
            if (hasIntentConnection) {
                val editRequest = IncidenceEditRequest(
                    incidenceID, incidenceTitle, incidenceDate, incidenceTime,
                    incidenceArea, incidenceType, incidenceDetail
                )
                Log.i("viewModel", "editIncidence: $editRequest")
                val response = repo.editIncidence(editRequest)
                handleEditResource(response)
            } else _incidenceEdit.postValue(
                    Resource.error(
                        data = _incidenceEdit.value?.data,
                        msg = "No Internet Connection "
                    )
                )

        } catch (e: Exception) {
            _incidenceEdit.postValue(
                Resource.error(
                    data = _incidenceEdit.value?.data,
                    msg = e.message ?: "Something went wrong"
                )
            )
        }


    }

    private fun handleEditResource(response: Response<IncidenceEditResponse>) {
        if (response.isSuccessful) {
            _incidenceEdit.postValue(Resource.success(response.body()!!))
        } else {
            var obj = JSONObject(response.errorBody()?.string())
            _incidenceEdit.postValue(
                Resource.error(
                    data = _incidenceEdit.value?.data,
                    msg = obj.getString("message") ?: "Error ${response.code()} found...!"
                )
            )
        }

    }



    private val _incidenceReport = MutableLiveData<Resource<IncidenceReportResponse?>>()
    var incidenceReport: LiveData<Resource<IncidenceReportResponse?>> = _incidenceReport

    fun incidenceReport(
        incidenceType: String?, incidenceTitle: String?, incidenceDate: String?,
        incidenceTime: String?, incidenceArea: String?, incidenceDetail: String?,
        projectCode: String?, userId: String?, photos: List<Photo>?
    ) = viewModelScope.launch {
        initiallyCalledReport = true
        delay(100)
        _incidenceReport.postValue(Resource.loading(_incidenceReport.value?.data))
        try {
            delay(100)
            if (hasIntentConnection) {
                val reportRequest = IncidenceReportRequest(incidenceArea, incidenceDate, incidenceDetail,
                    incidenceTitle, photos, projectCode, incidenceTime, incidenceType, userId)
                Log.i("viewModel", "reportIncidence: $reportRequest")
                val response=repo.reportIncidence(reportRequest)
                Log.i("Response", "incidenceReport: ${response.body()?.message}")
                handleReportResource(response)
            }
            else _incidenceReport.postValue(
                    Resource.error(
                        data=_incidenceReport.value?.data,
                        msg="No Internet Connection "
                    )
                )

        } catch (e: Exception) {
            _incidenceReport.postValue(
                Resource.error(
                    data = _incidenceReport.value?.data,
                    msg=e.message?:"Something went wrong "
                )
            )

        }
    }

    private fun handleReportResource(response: Response<IncidenceReportResponse>) {
        if(response.isSuccessful){
            _incidenceReport.postValue(Resource.success(response.body()!!))
            Log.i("message", "handleReportResourse: ${response.body()?.message}")
        }
        else{
            var obj=JSONObject(response.errorBody()?.string())
            _incidenceReport.postValue(
                Resource.error(
                    data=_incidenceReport.value?.data,
                    msg=obj?.getString("message")?:"Error ${response.code()} found...!"
                )
            )
            Log.i("message", "handleReportResource: ${response.body()?.message}")
        }
    }


}

