package com.norkut.orkesta_mobile.repository.service

import com.norkut.orkesta_mobile.domain.client.Client
import com.norkut.orkesta_mobile.domain.documentType.DocumentType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DocumentTypeService {

    @GET("DocumentType")
    suspend fun getDocumentTypeList(
        @Query("idDocumentType") idDocumentType: Int
    ): Response<List<DocumentType>>
}