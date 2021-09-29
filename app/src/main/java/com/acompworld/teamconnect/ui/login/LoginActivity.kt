package com.acompworld.teamconnect.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.acompworld.teamconnect.api.model.responses.LoginResponse
import com.acompworld.teamconnect.databinding.ActivityLoginBinding
import com.acompworld.teamconnect.ui.MainActivity
import com.acompworld.teamconnect.utils.Resource
import com.acompworld.teamconnect.utils.SharedPref
import com.acompworld.teamconnect.utils.Utils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        initViews()
        //setContentView(R.layout.activity_login)

        binding.loginLayout.submitBtn.setOnClickListener {
            login()
        }

        //val rootView: View =  getWindow().getDecorView().getRootView()
        viewModel.login.observe({lifecycle}) { handleResources(it)}
    }

    private fun initViews() {
        binding.loginLayout.submitBtn.setText("Login")
        binding.passwordEt.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                login()
            }
            false
        }
        if(SharedPref.getSavedUserData(this) != null){
            moveToMain()
        }
    }

    private fun login(){
        try{
             viewModel.login(
                binding.empnoEt.text.toString().trim(),
                binding.passwordEt.text.toString().trim()
            )

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun setUpViewModel() {
        /*viewModel =
            ViewModelProviders.of(
                this,
                ViewModelProviderFactory(LoginViewModel::class) {
                    LoginViewModel(LoginRepository(APIClinet.authApi), this)
                }
            ).get(LoginViewModel::class.java)*/
    }

    private fun initBindings() {
        try {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: IllegalStateException) {
        } catch (e: Exception) {
        }

    }

    private fun handleResources(res: Resource<LoginResponse>){
        when (res) {
            is Resource.loading -> {
                binding?.progressCircular?.isVisible = true
            }
            is Resource.error -> {
                binding?.progressCircular?.isVisible = false
                //res.data?.let { setUpViews(it) }
                Toast.makeText(this,res.msg?: "Something went wrong", Toast.LENGTH_SHORT).show()
            }
            is Resource.success -> {
                //setUpViews(res.data!!)
                SharedPref.saveUserData(this,res.data!!)
                binding?.progressCircular?.isVisible = false
                moveToMain()
            }
        }
    }

    private fun moveToMain(){
        Utils.moveTo(this,MainActivity::class.java)
    }
}