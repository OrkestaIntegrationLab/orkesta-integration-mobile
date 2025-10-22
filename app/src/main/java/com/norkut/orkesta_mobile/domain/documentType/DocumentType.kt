package com.norkut.orkesta_mobile.domain.documentType

import com.google.gson.annotations.SerializedName

data class DocumentType(
    @SerializedName("idDocumentType") var idDocumentType: Int = -1,
    @SerializedName("documentTypeName") var documentTypeName: String = "",
    @SerializedName("identifier") var identifier: String = "",
    @SerializedName("group") var group: String = "",
)