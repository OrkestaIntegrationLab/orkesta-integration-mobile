package com.norkut.orkesta_mobile.domain.client

import com.google.gson.annotations.SerializedName

data class ClientFilter(

    @SerializedName("idDocumentType")
      var idDocumentType: Int = -1,

    @SerializedName("documentNumber")
      var documentNumber: String = "",
)