package com.norkut.orkesta_mobile.domain.documentType

interface IDocumentTypeRepository {

    suspend fun getDocumentTypeList(filter: DocumentTypeFilter): List<DocumentType>

}