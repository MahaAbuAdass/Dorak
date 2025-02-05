package com.example.dorak.network



import com.example.dorak.dataclass.BranchResponse
import com.example.dorak.dataclass.LoginSucessResponse
import com.example.dorak.dataclass.ServicesResponse
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

}