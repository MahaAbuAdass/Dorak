package com.example.dorak.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
@Serializable
data class BranchResponse(
    @SerialName("BranchNameAr") var BranchNameAr: String? = null,
    @SerialName("BranchNameEn") var BranchNameEn: String? = null,
    @SerialName("BranchCode") var BranchCode: Int? = null,
    @SerialName("longitude") var longitude: String? = null,
    @SerialName("latitude") var latitude: String? = null,
    @SerialName("WorkingTimeStartsAt") var WorkingTimeStartsAt: String? = null,
    @SerialName("WorkingTimeEndsAt") var WorkingTimeEndsAt: String? = null
) : Parcelable
