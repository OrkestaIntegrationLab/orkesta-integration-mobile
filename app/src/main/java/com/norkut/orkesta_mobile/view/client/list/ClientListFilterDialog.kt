package com.norkut.orkesta_mobile.view.client.list

import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.norkut.orkesta_mobile.R
import com.norkut.orkesta_mobile.databinding.FragmentClientFilterBinding
import com.norkut.orkesta_mobile.domain.client.ClientFilter
import com.norkut.orkesta_mobile.domain.documentType.DocumentType
import kotlin.toString

typealias OnDateValidated = (Boolean) -> Unit

class ClientListFilterDialog (
    context: Context,
    layoutInflater: LayoutInflater
):  MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_rounded) {

    private var modalBinding = FragmentClientFilterBinding.inflate(layoutInflater)
    private lateinit var validationCallback: OnDateValidated
    var filter = ClientFilter()


    override fun create(): AlertDialog {
        setView(modalBinding.root)
        return super.create()
    }



    fun setDocumentTypes(documents: List<DocumentType>) = this.also {
          val values = documents.map {  String.format("%s - %s", it.identifier, it.documentTypeName) }
          with(modalBinding){
              etDocumentType.setAdapter(arrayAdapter(values))
              etDocumentType.setOnItemClickListener { _, _, position, _ ->
                  filter.idDocumentType = documents[position].idDocumentType
                  filter.documentNumber = documents[position].documentTypeName
              }
          }
      }

    fun setDocumentNumber() = this.also {
        modalBinding.etDocumentNumber.addTextChangedListener { filter.documentNumber = it.toString() }
    }



    private fun arrayAdapter(statusValues: List<String>) =
        ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, statusValues)

    private fun TextInputEditText.setOnGainFocusListener(listener: () -> Unit) {
        setOnFocusChangeListener { view, hasFocus ->
            if (view == this && hasFocus) listener()
        }
    }


}