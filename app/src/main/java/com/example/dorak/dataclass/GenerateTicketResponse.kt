package com.example.dorak.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GenerateTicketResponse (

    @SerializedName("TicketUniqueID"                         ) var TicketUniqueID                         : Int?    = null,
    @SerializedName("BranchNameEn"                           ) var BranchNameEn                           : String? = null,
    @SerializedName("BranchNameAr"                           ) var BranchNameAr                           : String? = null,
    @SerializedName("RegCount"                               ) var RegCount                               : Int?    = null,
    @SerializedName("ServCount"                              ) var ServCount                              : Int?    = null,
    @SerializedName("TicketNo"                               ) var TicketNo                               : String? = null,
    @SerializedName("LastTicketNo"                           ) var LastTicketNo                           : String? = null,
    @SerializedName("ReferenceUniqueID"                      ) var ReferenceUniqueID                      : String? = null,
    @SerializedName("QReferenceNo"                           ) var QReferenceNo                           : String? = null,
    @SerializedName("ReferencedQID"                          ) var ReferencedQID                          : Int?    = null,
    @SerializedName("LimitOfQReferencesPerDay"               ) var LimitOfQReferencesPerDay               : Int?    = null,
    @SerializedName("ValidityOfReferencesInDays"             ) var ValidityOfReferencesInDays             : Int?    = null,
    @SerializedName("ReferenceExpiryDate"                    ) var ReferenceExpiryDate                    : String? = null,
    @SerializedName("QRefNoAvailable"                        ) var QRefNoAvailable                        : String? = null,
    @SerializedName("RefTicketIssueAllowedFromDateAvailable" ) var RefTicketIssueAllowedFromDateAvailable : String? = null,
    @SerializedName("RefTicketIssueAllowedFromTimeAvailable" ) var RefTicketIssueAllowedFromTimeAvailable : String? = null,
    @SerializedName("QCustomNoteEn_NewTicket"                ) var QCustomNoteEnNewTicket                 : String? = null,
    @SerializedName("QCustomNoteAr_NewTicket"                ) var QCustomNoteArNewTicket                 : String? = null,
    @SerializedName("QCustomNoteEn_NewQReferenceTicket"      ) var QCustomNoteEnNewQReferenceTicket       : String? = null,
    @SerializedName("QCustomNoteAr_NewQReferenceTicket"      ) var QCustomNoteArNewQReferenceTicket       : String? = null,
    @SerializedName("BranchCustomNoteEn_NewTicket"           ) var BranchCustomNoteEnNewTicket            : String? = null,
    @SerializedName("BranchCustomNoteAr_NewTicket"           ) var BranchCustomNoteArNewTicket            : String? = null,
    @SerializedName("BranchCustomNoteEn_NewQReferenceTicket" ) var BranchCustomNoteEnNewQReferenceTicket  : String? = null,
    @SerializedName("BranchCustomNoteAr_NewQReferenceTicket" ) var BranchCustomNoteArNewQReferenceTicket  : String? = null,
    @SerializedName("Msg_Id"                                 ) var MsgId                                  : Int?    = null

): Parcelable