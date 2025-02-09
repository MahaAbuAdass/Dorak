package com.example.dorak.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dorak.dataclass.BookTicketResponse
import com.example.dorak.dataclass.GenerateTicketResponse
import com.example.dorak.network.RetrofitBuilder
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BookTicketViewModel (context: Context) : ViewModel() {

    private val retrofitBuilder = RetrofitBuilder(context)

    private val _bookTicketResponse = MutableLiveData<BookTicketResponse>()
    val bookTicketResponse: LiveData<BookTicketResponse> = _bookTicketResponse


    private val _errorResponse = MutableLiveData<String?>()
    val errorResponse: LiveData<String?> = _errorResponse


    suspend fun bookTicket(
        apptDate: String,
        qID: String,
        branchCode: String,
        user_id: String,
        app_time: String
    ) {
        viewModelScope.launch {
            try {
                val response = retrofitBuilder.bookTicket(apptDate, qID, branchCode, user_id, app_time)
                _bookTicketResponse.postValue(response)
            } catch (e: HttpException) {
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