package com.example.dorak.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WaitingCountResponse (

    @SerialName("WaitingCount"     ) var WaitingCount     : String? = null,
    @SerialName("avgWaitedMinutes" ) var avgWaitedMinutes : String? = null

)