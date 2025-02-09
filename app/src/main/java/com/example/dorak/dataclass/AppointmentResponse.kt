package com.example.dorak.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class AppointmentResponse (

    @SerializedName("ApptNo"       ) var ApptNo       : String? = null,
    @SerializedName("BranchNameAr" ) var BranchNameAr : String? = null,
    @SerializedName("QNameAr"      ) var QNameAr      : String? = null,
    @SerializedName("BranchNameEn" ) var BranchNameEn : String? = null,
    @SerializedName("QNameEn"      ) var QNameEn      : String? = null,
    @SerializedName("ApptDate"     ) var ApptDate     : String? = null

): Parcelable