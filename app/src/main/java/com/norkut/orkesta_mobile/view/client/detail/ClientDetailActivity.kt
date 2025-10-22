package com.norkut.orkesta_mobile.view.client.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.norkut.orkesta_mobile.R
import com.norkut.orkesta_mobile.common.fragments.EmptyFragment
import com.norkut.orkesta_mobile.common.utils.UI
import com.norkut.orkesta_mobile.databinding.ActivityClientDetailBinding
import com.norkut.orkesta_mobile.domain.client.Client
import com.norkut.orkesta_mobile.domain.documentType.DocumentType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ClientDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientDetailBinding
    private val viewModel: ClientDetailViewModel by  viewModels()
    private var _selectedItemIdMenu: Int = 0
    private lateinit var errorFragment: EmptyFragment
    private var argClientGeneral: Client = Client()
    private lateinit var clientGeneral: Client

    var selectedDocumentType: DocumentType? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setArguments(intent)
        observe()
        setupView()
        setAddress()
        loadDocumentTypeList()
        setOnClickListeners()
    }

    private fun observe(){
        observeDocumentTypeList()
        observeSaveClient()
    }

    private fun setOnClickListeners() = with(binding) {
        fabAdd.setOnClickListener {
         validateDataToSave()
        }
    }

    private fun validateDataToSave(){

        val isFormValid = validateFormFields(
            context = applicationContext,
            documentType = binding.etDocumentType.text?.toString(),
            documentNumber = binding.tiNewClientDocument.text?.toString(),
            name = binding.etClientName.text?.toString(),
            lastname = binding.etClientLastname.text?.toString(),
            mobile = binding.etMobileNumber.text?.toString(),
            email = binding.etEmail.text?.toString(),
            address = binding.etAddress.text?.toString(),
            layoutRefs = mapOf(
                "documentType" to binding.tiDocumentType,
                "documentNumber" to binding.tilNewClientDocument,
                "name" to binding.tiClientName,
                "lastname" to binding.tiClientLastname,
                "mobile" to binding.tiMobileNumber,
                "email" to binding.tiEmail,
                "address" to binding.taAddress
            )
        )

        if (isFormValid) {

            val clientModel = Client(
                idClient =  if (clientGeneral.idClient != -1) clientGeneral.idClient else -1,
                idDocumentType = selectedDocumentType?.idDocumentType ?: -1,
                firstName = binding.etClientName.text?.toString().orEmpty(),
                lastName = binding.etClientLastname.text?.toString().orEmpty(),
                documentNumber = binding.tiNewClientDocument.text?.toString().orEmpty(),
                email = binding.etEmail.text?.toString().orEmpty(),
                phone = binding.etMobileNumber.text?.toString().orEmpty(),
                address = binding.etAddress.text?.toString().orEmpty()
            )
            saveClient(clientModel)

        } else {
            Toast.makeText(this, "Existen errores en el formulario", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveClient(model : Client){
        Log.i("Client:", model.toString())
        //showLoading(true)
        viewModel.save(model)
    }

    fun validateFormFields(
        context: Context,
        documentType: String?,
        documentNumber: String?,
        name: String?,
        lastname: String?,
        mobile: String?,
        email: String?,
        address: String?,
        layoutRefs: Map<String, TextInputLayout>
    ): Boolean {
        var isValid = true

        fun setError(key: String, message: String) {
            layoutRefs[key]?.error = message
            isValid = false
        }

        // Validaciones
        if (documentType.isNullOrBlank()) setError("documentType", "Selecciona el tipo de documento")
        if (documentNumber.isNullOrBlank()) setError("documentNumber", "Ingresa el número de documento")
        if (name.isNullOrBlank()) setError("name", "Ingresa el nombre")
        if (lastname.isNullOrBlank()) setError("lastname", "Ingresa el apellido")
        if (mobile.isNullOrBlank() || !mobile.matches(Regex("^\\+584\\d{9}$")))
            setError("mobile", "Formato inválido. Ejemplo: +584262894428")
        if (email.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            setError("email", "Correo inválido. Ejemplo: usuario@dominio.com")
        if (address.isNullOrBlank()) setError("address", "Ingresa la dirección")

        return isValid
    }



    private fun setupView() = with(binding) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setClientData()
       /* topAppBar.title =  getString(R.string.origin_destiny_short_hour_route,checkInGeneral.originPortCode ,
            checkInGeneral.destinyPortCode ,
            checkInGeneral.itineraryDate.toLocalDateTimeStr("hh:mm a") )
        topAppBar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })
        openFragment(defaultFragment())*/
    }

    private fun setClientData(){
            if(clientGeneral.idClient != -1){
                with(binding){

                    tiNewClientDocument.setText(clientGeneral.documentNumber)
                    etClientName.setText(clientGeneral.firstName)
                    etClientLastname.setText(clientGeneral.lastName)
                    etMobileNumber.setText(clientGeneral.phone)
                    etEmail.setText(clientGeneral.email)
                    etAddress.setText(clientGeneral.address)

                }
            }
    }

    fun setAddress() = this.also {
        val maxLength = 100
        with(binding){
            etAddress.addTextChangedListener { clientGeneral.address = it.toString() }
            etAddress.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val currentLength = s?.length ?: 0
                    tvCharCount.text = applicationContext.getString(R.string.char_count_edit_text, "$currentLength","$maxLength")
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

    }


    private fun setArguments(intent: Intent) {
        intent.extras?.let {
            argClientGeneral = it.getParcelable(ARG_CLIENT) ?: Client()
        }
        clientGeneral = argClientGeneral
    }



    private fun loadDocumentTypeList(){
        viewModel.loadDocumentTypeIfNeeded()
    }

    private fun observeDocumentTypeList() {
        viewModel.documentTypeList.observe(this) {
            it?.let {
                setupListResult(it)
            }
        }
    }

    private fun setupListResult(list: List<DocumentType>) {
          with(binding){
              // showLoading(false)
              val values = list.map { it.identifier }
              etDocumentType.setAdapter(arrayAdapter(values))
              etDocumentType.setOnItemClickListener { _, _, position, _ ->
                  tiDocumentType.error = null
                  selectedDocumentType = list[position]
              }

            //Si es una edición
            if(clientGeneral.idClient!=-1){
                val index = list.indexOfFirst { it.idDocumentType == clientGeneral.idDocumentType }
                if (index != -1) {
                    etDocumentType.setText(values[index], false) // false evita que se dispare el listener
                    selectedDocumentType = list[index]
                }
             }
          }
    }

    fun isValidVenezuelanPhone(number: String): Boolean {
        val regex = Regex("^\\+584\\d{9}$")
        return regex.matches(number)
    }

    private fun observeSaveClient() {
        viewModel.saveClientResponse.observe(this) Observer@{ response ->
            response.idResponse?.let {
                if (it >= 0) {
                    refresh()
                    UI.showToast(this, getString(R.string.client_save_success), Toast.LENGTH_LONG)
                } else {
                    refresh()
                    UI.showToast(this, getString(R.string.error_save_client), Toast.LENGTH_LONG)
                }
            }
            response.error?.let {
                refresh()
                //showLoading(false)
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun refresh(){
        onBackPressedDispatcher.onBackPressed()
    }





    private fun arrayAdapter(statusValues: List<String>) =
        ArrayAdapter(applicationContext, android.R.layout.simple_dropdown_item_1line, statusValues)

    private fun TextInputEditText.setOnGainFocusListener(listener: () -> Unit) {
        setOnFocusChangeListener { view, hasFocus ->
            if (view == this && hasFocus) listener()
        }
    }
    companion object {
        const val ARG_CLIENT = "argClient"
    }
}