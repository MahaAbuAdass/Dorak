package com.example.dorak.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServicesResponse (

    @SerialName("Qid"     ) var Qid     : Int?    = null,
    @SerialName("QNameEn" ) var QNameEn : String? = null,
    @SerialName("QNameAr" ) var QNameAr : String? = null

)