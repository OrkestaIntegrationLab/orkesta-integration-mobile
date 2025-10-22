package com.norkut.orkesta_mobile.domain.client

import com.google.gson.annotations.SerializedName

data class ClientResult(
    @SerializedName("idResponse")  var idResponse: Long = -1,
    @SerializedName("transferResult")  var transferResult: String = ""
)
