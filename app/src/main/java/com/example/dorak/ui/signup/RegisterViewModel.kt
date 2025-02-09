package com.example.dorak.ui.signup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dorak.dataclass.AppointmentResponse
import com.example.dorak.dataclass.RegisterResponse
import com.example.dorak.network.RetrofitBuilder
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel (context: Context) : ViewModel()  {

    private val retrofitBuilder = RetrofitBuilder(context)

    private val _registrationResponse = MutableLiveData<RegisterResponse>()
    val registrationResponse: LiveData<RegisterResponse> = _registrationResponse


    private val _errorResponse = MutableLiveData<String?>()
    val errorResponse: LiveData<String?> = _errorResponse


    suspend fun registerUser( fullNameEn: String,
                              address: String,
                              emailID: String,
                              contactMobileNum: String,
                              nationalIDNo: String,
                              loginStatus: Int,
                              contactUserId: String,
                              sex: String,
                              dob: String,
                              nationalityEn: String,
                              nationalityAr: String,
                              postalCode: String,
                              username: String,
                              txtPassword: String,
                              mrn: String,
                              fullNameAR: String){
        viewModelScope.launch {
            try {
                val response = retrofitBuilder.registerUser(fullNameEn, address, emailID, contactMobileNum, nationalIDNo, loginStatus, contactUserId, sex, dob, nationalityEn, nationalityAr, postalCode, username, txtPassword, mrn, fullNameAR)
                _registrationResponse.postValue(response)
            } catch (e: HttpException){
                handleHttpException(e)
            } catch (e: Exception) {
                // Handle other exceptions
                _errorResponse.postValue("Unexpected error occurred: ${e.message}")
            }
        }
    }

    private fun handleHttpException(e: HttpException) {
        when (e.code()) {
            400 -> _errorResponse.postValue("Bad Request: ${e.message}")
            401 -> _errorResponse.postValue("Unauthorized: ${e.message}")
            403 -> _errorResponse.postValue("Forbidden: ${e.message}")
            404 -> _errorResponse.postValue("Not Found: ${e.message}")
            500 -> _errorResponse.postValue("Internal Server Error: ${e.message}")
            503 -> _errorResponse.postValue("Service Unavailable: ${e.message}")
            else -> _errorResponse.postValue("HTTP error: ${e.code()} - ${e.message}")
        }
    }

}