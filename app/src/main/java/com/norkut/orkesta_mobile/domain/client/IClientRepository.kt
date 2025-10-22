package com.norkut.orkesta_mobile.domain.client

import com.norkut.orkesta_mobile.domain.documentType.DocumentType
import com.norkut.orkesta_mobile.domain.documentType.DocumentTypeFilter

interface IClientRepository {
    suspend fun getClientList(filter: ClientFilter): List<Client>
    suspend fun saveClient(model : Client) : Long
}