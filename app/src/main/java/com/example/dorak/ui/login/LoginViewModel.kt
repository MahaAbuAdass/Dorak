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

class LoginViewModel (context: Context) : ViewModel() {

    private val retrofitBuilder = RetrofitBuilder(context)

    private val _getLoginResponse = MutableLiveData<LoginSucessResponse?>()
    val getLoginResponse: LiveData<LoginSucessResponse?> = _getLoginResponse

    private val _errorResponse = MutableLiveData<LoginErrorResponse?>()
    val errorResponse: LiveData<LoginErrorResponse?> = _errorResponse

    fun callLoginApi(userName: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Reset previous responses before calling API
            _getLoginResponse.postValue(null)
            _errorResponse.postValue(null)

            runCatching {
                retrofitBuilder.login(userName, password) // Call API
            }.onSuccess { response ->
                Log.d("LoginAPI", "Raw API response: $response")

                // ✅ Check if the response follows the success structure
                if (response != null && response.FullNameEn != null) {
                    _getLoginResponse.postValue(response) // ✅ Success case
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
                handleException(exception) // Handle errors
            }
        }
    }






    private fun handleException(exception: Throwable) {
        when (exception) {
            is HttpException -> {
                if (exception.code() == 404) {
                    try {
                        val errorBody = exception.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, LoginErrorResponse::class.java)
                        _errorResponse.postValue(errorResponse) // ❌ Show error message from API
                    } catch (ex: Exception) {
                        _errorResponse.postValue(
                            LoginErrorResponse("HTTP Error", 404, "User not found", "المستخدم غير موجود", null)
                        )
                    }
                } else {
                    handleHttpException(exception) // Handle other HTTP errors
                }
            }
            is IOException -> _errorResponse.postValue(
                LoginErrorResponse("Network Error", null, "No Internet Connection", "لا يوجد اتصال بالإنترنت", null)
            )
            else -> _errorResponse.postValue(
                LoginErrorResponse("Unexpected Error", null, exception.message, exception.message, null)
            )
        }
    }


    private fun handleHttpException(e: HttpException) {
        try {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginErrorResponse::class.java)
            _errorResponse.postValue(errorResponse)
        } catch (ex: Exception) {
            // Handle any other exceptions during error handling
            _errorResponse.postValue(
                LoginErrorResponse("HTTP Error", e.code(), "Error: ${e.message()}", "خطأ: ${e.message()}", null)
            )
        }
    }
}
