package com.example.dorak.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Parcelize
@Serializable
data class MyTicketResponse (

    @SerialName("TicketNo"     ) var TicketNo     : String? = null,
    @SerialName("RegTime"      ) var RegTime      : String? = null,
    @SerialName("QNameAr"      ) var QNameAr      : String? = null,
    @SerialName("BranchNameAr" ) var BranchNameAr : String? = null

): Parcelable