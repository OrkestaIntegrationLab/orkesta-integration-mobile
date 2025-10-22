package com.norkut.orkesta_mobile.repository.remote

import com.norkut.orkesta_mobile.domain.client.Client
import com.norkut.orkesta_mobile.domain.client.ClientFilter
import com.norkut.orkesta_mobile.domain.client.IClientRepository
import com.norkut.orkesta_mobile.repository.service.ClientService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClientRepository @Inject constructor(private val service : ClientService) : IClientRepository , RemoteRepository() {


    override suspend fun getClientList(filter: ClientFilter) = withContext(Dispatchers.IO){
        service.getClientList(
            filter.idDocumentType,
            documentNumber =  filter.documentNumber
        ).validate()!!
    }

    override suspend fun saveClient(model: Client) = withContext(Dispatchers.IO) {
        val response = service.updateClient(model, 1)
        if (response.isSuccessful)
            response.body()!!
        else throw Exception(response.message())
    }

}