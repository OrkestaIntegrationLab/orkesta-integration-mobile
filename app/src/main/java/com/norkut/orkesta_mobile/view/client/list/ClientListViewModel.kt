package com.norkut.orkesta_mobile.view.client.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norkut.orkesta_mobile.R
import com.norkut.orkesta_mobile.common.utils.FormState
import com.norkut.orkesta_mobile.domain.client.Client
import com.norkut.orkesta_mobile.domain.client.ClientFilter
import com.norkut.orkesta_mobile.domain.client.ClientResponse
import com.norkut.orkesta_mobile.domain.client.IClientRepository
import com.norkut.orkesta_mobile.domain.documentType.DocumentType
import com.norkut.orkesta_mobile.domain.documentType.DocumentTypeFilter
import com.norkut.orkesta_mobile.domain.documentType.IDocumentTypeRepository
import com.norkut.orkesta_mobile.repository.remote.DocumentTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ClientListViewModel @Inject constructor( private val clientRepository: IClientRepository ,
                                               private val documentTypeRepository: IDocumentTypeRepository) : ViewModel(){

    private val _clientList = MutableLiveData<List<Client>>()
    val clientList: LiveData<List<Client>> = _clientList

    private val _state = MutableLiveData<FormState>()
    val state: LiveData<FormState> = _state

    private val _documentTypeList = MutableLiveData<List<DocumentType>>()
    val documentTypeList: LiveData<List<DocumentType>> = _documentTypeList

    fun loadDocumentTypeIfNeeded() {
        if (_documentTypeList.value.isNullOrEmpty().not()) return
            viewModelScope.launch { getDocumentTypeList() }
    }


    fun getClientList(filter: ClientFilter) {
        viewModelScope.launch {
            try {
                val list = clientRepository.getClientList(filter)
                _clientList.postValue(list)
            } catch (ex: Exception) {
                 Log.i("error", ex.toString())
                 ClientResponse(error = R.string.error_load_client , clientList = emptyList())
            }
        }
    }



    fun getDocumentTypeList() {
        viewModelScope.launch {
            try {
                var filter = DocumentTypeFilter(idDocumentType = -1 )
                val list  = documentTypeRepository.getDocumentTypeList(filter)
                _documentTypeList.value = list
            } catch (ex: Exception) {
                _state.value = FormState(globalInfo = R.string.main_error_get)
            }
        }
    }



}