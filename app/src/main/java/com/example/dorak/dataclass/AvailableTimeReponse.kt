package com.example.dorak.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class AvailableTimeReponse (

    @SerializedName("SID"               ) var SID               : Int?    = null,
    @SerializedName("DateEffectiveFrom" ) var DateEffectiveFrom : String? = null

): Parcelable