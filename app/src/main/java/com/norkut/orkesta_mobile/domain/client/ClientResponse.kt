package com.norkut.orkesta_mobile.domain.client

data class ClientResponse(
    val clientList: List<Client>,
    val error: Int? = null
)
