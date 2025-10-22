package com.norkut.orkesta_mobile.domain.client

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    @SerializedName("idClient") var idClient: Int = -1,
    @SerializedName("idDocumentType") var idDocumentType: Int = -1,
    @SerializedName("firstName") var firstName: String = "",
    @SerializedName("lastName") var lastName: String = "",
    @SerializedName("documentNumber") var documentNumber: String = "",
    @SerializedName("documentIdentifier") var documentIdentifier: String = "",
    @SerializedName("documentTypeName") var documentTypeName: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("phone") var phone: String = "",
    @SerializedName("address") var address: String = "",
): Parcelable {
    fun getFullName(): String = "$firstName $lastName"
}
