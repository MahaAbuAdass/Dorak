package com.example.dorak.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dorak.dataclass.AvailableTimeReponse
import com.example.dorak.dataclass.BranchResponse
import com.example.dorak.dataclass.ServicesResponse
import com.example.dorak.network.RetrofitBuilder
import kotlinx.coroutines.launch
import retrofit2.HttpException

class GetAvailableTimeViewModel(context: Context) : ViewModel()  {

    private val retrofitBuilder = RetrofitBuilder(context)

    private val _availableTimeResponse = MutableLiveData<List<AvailableTimeReponse>>()
    val availableTimeResponse: LiveData<List<AvailableTimeReponse>> = _availableTimeResponse


    private val _errorResponse = MutableLiveData<String?>()
    val errorResponse: LiveData<String?> = _errorResponse


    suspend fun getAvailableTime(branchCode:String){
        viewModelScope.launch {
            try {
                val response = retrofitBuilder.getAvailableTime(branchCode)
                _availableTimeResponse.postValue(response)
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