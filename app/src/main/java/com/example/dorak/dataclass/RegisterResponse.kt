package com.example.dorak.dataclass

import com.google.gson.annotations.SerializedName

data class RegisterResponse (

    @SerializedName("Msg_Id"  ) var MsgId  : String? = null,
    @SerializedName("Msg_Str" ) var MsgStr : String? = null

)