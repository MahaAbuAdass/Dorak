package com.example.dorak.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class BookTicketResponse (

    @SerialName("Msg_Id"  ) var MsgId  : String? = null,
    @SerialName("Msg_Str" ) var MsgStr : String? = null,
    @SerialName("appno"   ) var appno  : String? = null

): Parcelable
