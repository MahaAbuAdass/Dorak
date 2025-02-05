package com.example.dorak.network


import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitBuilder(context: Context) {

    //lazy: define heavy variable as lazy to execute it when call it only

    private val apiService: ApiService by lazy {
      // val baseUrl = PreferenceManager.getBaseUrl(context)
      val baseUrl = "http://192.168.30.50/APIPub2509/"

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl?:"")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }


    suspend fun login(userName:String ,password:String) = apiService.login(userName,password)
    suspend fun getServices()=apiService.getAllServicesApi()

}


