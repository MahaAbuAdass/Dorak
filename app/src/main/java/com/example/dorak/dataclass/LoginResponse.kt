package com.example.dorak.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class LoginSucessResponse(
    @SerialName("FullNameAr" ) var FullNameAr : String? = null,
    @SerialName("FullNameEn" ) var FullNameEn : String? = null,
    @SerialName("Sex"        ) var Sex        : String? = null

): Parcelable

@Parcelize
data class LoginErrorResponse(
    @SerializedName("result") val result: String?,
    @SerializedName("error_code") val errorCode: Int?,
    @SerializedName("msg_en") val messageEn: String?,
    @SerializedName("msg_ar") val messageAr: String?,
    @SerializedName("User_Id") val userId: String?
): Parcelable
