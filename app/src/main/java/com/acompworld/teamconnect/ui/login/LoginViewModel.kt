package com.acompworld.teamconnect.ui.login

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acompworld.teamconnect.api.model.requests.LoginRequest
import com.acompworld.teamconnect.api.model.responses.LoginResponse
import com.acompworld.teamconnect.api.model.responses.MasterResponse
import com.acompworld.teamconnect.repo.TeamConnectRepository
import com.acompworld.teamconnect.utils.Resource
import com.acompworld.teamconnect.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val cm: ConnectivityManager,
    val repo: TeamConnectRepository
) : ViewModel() {

    private var initiallyCalledAboutUS = false

    private val builder = NetworkRequest.Builder()
    var hasIntentConnection: Boolean = false

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            hasIntentConnection = true
            //incase : app starts in offline mode  and then  on runtime net connects then this fetch data
            //if (_login.value?.data == null && initiallyCalledAboutUS) Login()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            hasIntentConnection = false
        }
    }

    private val _login = MutableLiveData<Resource<LoginResponse>>()
    var login: LiveData<Resource<LoginResponse>> = _login


    init {
        runBlocking { cm.registerNetworkCallback(builder.build(), networkCallback) }
    }

    fun login(empno: String = "", password: String = "") = viewModelScope.launch {
        initiallyCalledAboutUS = true
        _login.postValue((Resource.loading(_login.value?.data)))
        try {
            if(hasIntentConnection) {
                val loginRequest = LoginRequest(empno, password)
                val response = repo.login(loginRequest);
                handleLoginResource(response)
            }else _login.postValue(
                Resource.error(
                    data = _login.value?.data,
                    msg = "No Internet Connection"
                )
            )
        } catch (e: Exception) {
            _login.postValue(
                Resource.error(
                    data = _login.value?.data,
                    msg = e.message?: "Something went wrong"
                )
            )
        }
    }

    private fun handleLoginResource(response: Response<LoginResponse>) {
        if (response.isSuccessful) {
            _login.postValue(Resource.success(response.body()!!))
        }
        else {
            var obj = JSONObject(response.errorBody()?.string())

            _login.postValue(

                Resource.error(
                    data = _login.value?.data,
                    msg = obj?.getString("message") ?: "Error ${response.code()} found...!"
                )
            )
        }
    }
    /*private val _empnoField = MutableLiveData<String>();
    val empnoField: LiveData<String> = _empnoField

    private val _passwordField = MutableLiveData<String>();
    val passwordField: LiveData<String> = _passwordField;

    private val _empnoFieldValidation = MutableLiveData<Resource<Int>>()
    val emailFieldValidation: LiveData<Resource<Int>> = _empnoFieldValidation;

    private val _passwordFieldValidation = MutableLiveData<Resource<Int>>();
    val passwordFieldValidation: LiveData<Resource<Int>> = _passwordFieldValidation;

    fun onEmpnoChanged(empno:String) {
        _empnoField.value = empno
    }

    fun onPasswordChanged(password: String) {
        _passwordField.value = password
    }

    private fun isValidEmpno(empno:String) : Boolean {
        var isValidEmpno = false;
        if(empno.isEmpty()){
            _empnoFieldValidation.value = Resource.error(null, context.resources.getString(R.string.invalidEmpno))
        }
        else {
            isValidEmpno = (!TextUtils.isEmpty(empno))
            if(!isValidEmpno)
                _empnoFieldValidation.value = Resource.error(null, context.resources.getString(R.string.invalidEmpno))
            _empnoFieldValidation.value = Resource.success(null)
        }
        return isValidEmpno
    }

    private fun isPasswordValid(password: String): Boolean {
        var isValidPassword = false
        if (password.isEmpty()) {
            _passwordFieldValidation.value = Resource.error(null, context.resources.getString(R.string.invalidPassword))
        } else {
            isValidPassword = password.length > 7
            if (isValidPassword) {
                _passwordFieldValidation.value = Resource.success(null)
            } else {
                _passwordFieldValidation.value = Resource.error(null, context.resources.getString(R.string.invalidPassword))
            }
        }
        return isValidPassword
    }*/

}