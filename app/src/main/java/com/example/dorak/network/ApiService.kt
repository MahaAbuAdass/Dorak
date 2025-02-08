package com.example.dorak.network



import com.example.dorak.dataclass.AvailableTimeReponse
import com.example.dorak.dataclass.BranchResponse
import com.example.dorak.dataclass.GenerateTicketResponse
import com.example.dorak.dataclass.LoginSucessResponse
import com.example.dorak.dataclass.ServicesResponse
import com.example.dorak.dataclass.TimeSlotsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/MobileLogin")
    suspend fun login(
        @Query("txtUserName") txtUserName: String,
        @Query("txtPasswod") txtPasswod: String
    ): LoginSucessResponse


    @GET("api/App_GetAllService")
    suspend fun getAllServicesApi(
    ): List<ServicesResponse>

    @GET("api/App_GetAllBranchesByQID")
    suspend fun getBranchResopnse(
        @Query("QID") QID: String,
    ): List<BranchResponse>

    @GET("api/App_GetAvailableTimeApp")
    suspend fun getAvailableTime(
        @Query("BranchCode") BranchCode: String,
    ): List<AvailableTimeReponse>

    @GET("api/APPT_spGetSlots")
    suspend fun getTimeSlot(
        @Query("ApptDate") ApptDate: String,
        @Query("BranchCode") BranchCode: String,
        @Query("SID") SID: String,
    ): List<TimeSlotsResponse>


    @GET("api/SP_Mob_Q_Insert")
    suspend fun generateTicket(
        @Query("QID") QID: String,
        @Query("BranchCode") BranchCode: String,
        @Query("Login_User_ID") Login_User_ID: String,
    ): GenerateTicketResponse

}