package com.norkut.orkesta_mobile.dependencies

import com.norkut.orkesta_mobile.repository.service.ClientService
import com.norkut.orkesta_mobile.repository.service.DocumentTypeService
import com.norkut.orkesta_mobile.repository.service.ServiceBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideClientService(): ClientService {
        return ServiceBuilder.buildApi(ClientService::class.java,false)
    }

    @Provides
    fun provideDocumentTypeService(): DocumentTypeService {
        return ServiceBuilder.buildApi(DocumentTypeService::class.java,false)
    }
}