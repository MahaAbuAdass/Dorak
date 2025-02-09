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
            .baseUrl(baseUrl ?: "")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }


    suspend fun login(userName: String, password: String) = apiService.login(userName, password)
    suspend fun getServices() = apiService.getAllServicesApi()
    suspend fun getBranches(qId: String) = apiService.getBranchResopnse(qId)

    suspend fun getAvailableTime(branchCode: String) = apiService.getAvailableTime(branchCode)
    suspend fun getTimeSlots(apptDate: String, branchCode: String, sID: String) =
        apiService.getTimeSlot(apptDate, branchCode, sID)

    suspend fun generateTicket(qID: String, branchCode: String, user_id: String) =
        apiService.generateTicket(qID, branchCode, user_id)

    suspend fun bookTicket(
        apptDate: String,
        qID: String,
        branchCode: String,
        user_id: String,
        app_time: String
    ) = apiService.bookTicket(apptDate, qID, branchCode, user_id, app_time)

    suspend fun getWaitingCount(branchCode: String, qID: String) =
        apiService.getWaitingCount(branchCode, qID)

    suspend fun getMyTicket(userLogin: String) = apiService.getMyTicket(userLogin)

    suspend fun appointmentInfo(appno: String) = apiService.appointmentInfo(appno)

    suspend fun registerUser(
        fullNameEn: String,
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
        fullNameAR: String
    ) = apiService.registerUser(
        fullNameEn,
        address,
        emailID,
        contactMobileNum,
        nationalIDNo,
        loginStatus,
        contactUserId,
        sex,
        dob,
        nationalityEn,
        nationalityAr,
        postalCode,
        username,
        txtPassword,
        mrn,
        fullNameAR
    )
}


