package com.norkut.orkesta_mobile.repository.service

import com.norkut.orkesta_mobile.domain.client.Client
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ClientService {

    @GET("Client")
    suspend fun getClientList(
        @Query("idDocumentType") idDocumentType: Int,
        @Query("documentNumber") documentNumber: String
    ): Response<List<Client>>

    @POST("Client/{idUser}")
    suspend fun updateClient(
        @Body model: Client,
        @Path("idUser") idUser: Long
    ): Response<Long>


}