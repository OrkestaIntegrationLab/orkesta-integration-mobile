package com.norkut.orkesta_mobile.domain.client

import com.google.gson.annotations.SerializedName

data class Blank(
    @SerializedName("id") var id: Int = -1,
    @SerializedName("name") var name: String = "",
    )