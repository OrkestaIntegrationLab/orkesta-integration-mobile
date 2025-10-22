package com.norkut.orkesta_mobile.view.client.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norkut.orkesta_mobile.R
import com.norkut.orkesta_mobile.common.utils.FormState
import com.norkut.orkesta_mobile.domain.client.Client
import com.norkut.orkesta_mobile.domain.client.IClientRepository
import com.norkut.orkesta_mobile.domain.client.SaveClientResponse
import com.norkut.orkesta_mobile.domain.documentType.DocumentType
import com.norkut.orkesta_mobile.domain.documentType.DocumentTypeFilter
import com.norkut.orkesta_mobile.domain.documentType.IDocumentTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientDetailViewModel @Inject constructor (private val clientRepository: IClientRepository ,
                            private val documentTypeRepository: IDocumentTypeRepository) : ViewModel() {


    private val _documentTypeList = MutableLiveData<List<DocumentType>>()
    val documentTypeList: LiveData<List<DocumentType>> = _documentTypeList

    private var _saveClientResponse = MutableLiveData<SaveClientResponse>()
    val saveClientResponse: LiveData<SaveClientResponse> = _saveClientResponse

    fun loadDocumentTypeIfNeeded() {
        if (_documentTypeList.value.isNullOrEmpty().not()) return
        viewModelScope.launch { getDocumentTypeList() }
    }

    fun save(model : Client){
        viewModelScope.launch { _saveClientResponse.value = saveClient(model)}
    }

    fun getDocumentTypeList() {
        viewModelScope.launch {
            try {
                var filter = DocumentTypeFilter(idDocumentType = -1 )
                val list  = documentTypeRepository.getDocumentTypeList(filter)
                _documentTypeList.value = list
            } catch (ex: Exception) {
               // _state.value = FormState(globalInfo = R.string.main_error_get)
            }
        }
    }





    private suspend fun saveClient (model : Client): SaveClientResponse {
        return try {
            val result = clientRepository.saveClient(model)
            SaveClientResponse(idResponse = result , jsonResult = "")
        } catch (ex: Exception) {
            Log.e("updateClient",ex.message.toString(),ex)
            SaveClientResponse(error = R.string.main_error_client_post)
        }
    }
}