package com.example.dorak.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GenerateTicketResponse (

    @SerialName("TicketUniqueID" ) var TicketUniqueID : String? = null,
    @SerialName("BranchNameAr"   ) var BranchNameAr   : String? = null,
    @SerialName("BranchNameEn"   ) var BranchNameEn   : String? = null,
    @SerialName("TicketNo"       ) var TicketNo       : String? = null
): Parcelable