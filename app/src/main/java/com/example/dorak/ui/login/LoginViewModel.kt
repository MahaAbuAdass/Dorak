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

class LoginViewModel(context: Context) : ViewModel() {

    private val retrofitBuilder = RetrofitBuilder(context)

    private val _getLoginResponse = MutableLiveData<LoginSucessResponse?>()
    val getLoginResponse: LiveData<LoginSucessResponse?> = _getLoginResponse

    private val _errorResponse = MutableLiveData<LoginErrorResponse?>()
    val errorResponse: LiveData<LoginErrorResponse?> = _errorResponse

    fun callLoginApi(userName: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                retrofitBuilder.login(userName, password) // Call API
            }.onSuccess { response ->
                Log.d("LoginAPI", "✅ Success response received: $response")
                if (response.FullNameEn != null) {
                    _getLoginResponse.postValue(response) // ✅ Success case
                    _errorResponse.postValue(null) // Reset error since login succeeded
                } else {
                    _errorResponse.postValue(
                        LoginErrorResponse(
                            "Invalid Data",
                            400,
                            "Invalid credentials",
                            "بيانات غير صالحة",
                            null
                        )
                    )
                }
            }.onFailure { exception ->
                handleException(exception) // Handle API errors
            }
        }
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is HttpException -> {
                val code = exception.code()
                val errorMessage = exception.message()
                val errorBody = exception.response()?.errorBody()?.string()
                val errorResponse = try {
                    Gson().fromJson(errorBody, LoginErrorResponse::class.java)
                } catch (ex: Exception) {
                    LoginErrorResponse("HTTP Error", code, errorMessage, "خطأ: $errorMessage", null)
                }
                _errorResponse.postValue(errorResponse) // ❌ Post error to LiveData
                _getLoginResponse.postValue(null) // Reset success response on failure
            }
            is IOException -> _errorResponse.postValue(
                LoginErrorResponse("Network Error", null, "No Internet Connection", "لا يوجد اتصال بالإنترنت", null)
            )
            else -> _errorResponse.postValue(
                LoginErrorResponse("Unexpected Error", null, exception.message, exception.message, null)
            )
        }
    }
}
