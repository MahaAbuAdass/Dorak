package com.example.dorak.viewmodels


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dorak.dataclass.GenerateTicketResponse
import com.example.dorak.network.RetrofitBuilder
import kotlinx.coroutines.launch
import retrofit2.HttpException

class GenerateTicketViewModel(context: Context) : ViewModel()  {

    private val retrofitBuilder = RetrofitBuilder(context)

    private val _generateTicketResponse = MutableLiveData<GenerateTicketResponse>()
    val generateTicketResponse: LiveData<GenerateTicketResponse> = _generateTicketResponse


    private val _errorResponse = MutableLiveData<String?>()
    val errorResponse: LiveData<String?> = _errorResponse


    suspend fun getGeneratedTicket(qID: String,branchCode: String,user_id: String){
        viewModelScope.launch {
            try {
                val response = retrofitBuilder.generateTicket(qID, branchCode, user_id)

                if (response.TicketNo.isNullOrEmpty()) {
                    Log.e("API Error", "TicketNo is missing from response")
                    _errorResponse.postValue("Error: Ticket Number is missing")
                } else {
                    _generateTicketResponse.postValue(response)
                }

            } catch (e: HttpException){
                Log.v("response generate ticket error", e.message.toString())

                handleHttpException(e)
            } catch (e: Exception) {
                // Handle other exceptions
                Log.v("response generate ticket error", e.message.toString())

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