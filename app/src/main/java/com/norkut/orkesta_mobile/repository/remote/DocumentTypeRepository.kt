package com.norkut.orkesta_mobile.repository.remote

import com.norkut.orkesta_mobile.domain.documentType.DocumentType
import com.norkut.orkesta_mobile.domain.documentType.DocumentTypeFilter
import com.norkut.orkesta_mobile.domain.documentType.IDocumentTypeRepository
import com.norkut.orkesta_mobile.repository.service.DocumentTypeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DocumentTypeRepository @Inject constructor(private val service : DocumentTypeService) : IDocumentTypeRepository , RemoteRepository() {


    override suspend fun getDocumentTypeList(filter: DocumentTypeFilter) = withContext(Dispatchers.IO) {
        service.getDocumentTypeList(
            filter.idDocumentType
        ).validate()!!
    }

}