package com.example.dorak.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dorak.dataclass.LoginErrorResponse
import com.example.dorak.dataclass.LoginSucessResponse
import com.example.dorak.network.RetrofitBuilder
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Sealed class to represent login response states
sealed class LoginResponse {
    data class Success(val data: LoginSucessResponse) : LoginResponse()
    data class Error(val message: String, val code: Int? = null) : LoginResponse()
}

class LoginViewModel(context: Context) : ViewModel() {

    private val retrofitBuilder = RetrofitBuilder(context)

    // Single LiveData for login response
    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    // Flag to track if an API call is in progress
    private var isLoginInProgress = false

    fun callLoginApi(userName: String, password: String) {
        if (isLoginInProgress) {
            return // Prevent multiple API calls
        }

        isLoginInProgress = true // Set flag to indicate API call is in progress

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                retrofitBuilder.login(userName, password) // Call API
            }.onSuccess { response ->
                Log.d("LoginAPI", "✅ Success response received: $response")
                if (response.FullNameEn != null) {
                    _loginResponse.postValue(LoginResponse.Success(response)) // ✅ Success case
                } else {
                    _loginResponse.postValue(
                        LoginResponse.Error("Invalid credentials", 400) // ❌ Error case
                    )
                }
            }.onFailure { exception ->
                handleException(exception) // Handle API errors
            }.also {
                isLoginInProgress = false // Reset flag after API call completes
            }
        }
    }

    private fun handleException(exception: Throwable) {
        val errorResponse = when (exception) {
            is HttpException -> {
                val code = exception.code()
                val errorMessage = exception.message()
                val errorBody = exception.response()?.errorBody()?.string()
                try {
                    val error = Gson().fromJson(errorBody, LoginErrorResponse::class.java)
                    LoginResponse.Error(error.messageEn ?: "HTTP Error", code)
                } catch (ex: Exception) {
                    LoginResponse.Error("HTTP Error: $errorMessage", code)
                }
            }
            is IOException -> LoginResponse.Error("No Internet Connection")
            else -> LoginResponse.Error(exception.message ?: "Unexpected Error")
        }
        _loginResponse.postValue(errorResponse) // ❌ Post error to LiveData
    }

    // Reset LiveData after handling the response
    fun resetLoginResponse() {
        _loginResponse.value = null
    }
}