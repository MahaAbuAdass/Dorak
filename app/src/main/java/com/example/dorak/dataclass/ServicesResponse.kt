package com.example.dorak.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ServicesResponse (

    @SerialName("Qid"     ) var Qid     : Int?    = null,
    @SerialName("QNameEn" ) var QNameEn : String? = null,
    @SerialName("QNameAr" ) var QNameAr : String? = null

): Parcelable