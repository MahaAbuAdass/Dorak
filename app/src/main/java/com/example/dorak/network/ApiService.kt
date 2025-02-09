package com.example.dorak.network



import com.example.dorak.dataclass.AvailableTimeReponse
import com.example.dorak.dataclass.BookTicketResponse
import com.example.dorak.dataclass.BranchResponse
import com.example.dorak.dataclass.GenerateTicketResponse
import com.example.dorak.dataclass.LoginSucessResponse
import com.example.dorak.dataclass.MyTicketResponse
import com.example.dorak.dataclass.ServicesResponse
import com.example.dorak.dataclass.TimeSlotsResponse
import com.example.dorak.dataclass.WaitingCountResponse
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
        @Query("BranchCode") branchCode: String,
        @Query("Login_User_ID") user_id: String,
    ): GenerateTicketResponse

    @GET("api/APPT_SP_IUD")
    suspend fun bookTicket(
        @Query("ApptDate") apptDate: String,
        @Query("QID") qID: String,
        @Query("BranchCode") branchCode: String,
        @Query("Login_User_ID") user_id: String,
        @Query("App_time") app_time: String,
        ): BookTicketResponse


    @GET("api/App_GetWaitingCountAndEstimate")
    suspend fun getWaitingCount(
        @Query("branchCode") branchCode: String,
        @Query("QID") qID: String,
    ): WaitingCountResponse


    @GET("api/App_GetMyTicket")
    suspend fun getMyTicket(
        @Query("UserLogin") userLogin: String
    ): List<MyTicketResponse>


}