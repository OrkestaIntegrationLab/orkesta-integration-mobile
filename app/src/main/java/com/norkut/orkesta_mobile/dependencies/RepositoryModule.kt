package com.norkut.orkesta_mobile.dependencies

import com.norkut.orkesta_mobile.domain.client.IClientRepository
import com.norkut.orkesta_mobile.domain.documentType.IDocumentTypeRepository
import com.norkut.orkesta_mobile.repository.remote.ClientRepository
import com.norkut.orkesta_mobile.repository.remote.DocumentTypeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideDocumentTypeRepository(implementation: DocumentTypeRepository): IDocumentTypeRepository

    @Binds
    abstract fun provideClientRepository(implementation: ClientRepository): IClientRepository

}