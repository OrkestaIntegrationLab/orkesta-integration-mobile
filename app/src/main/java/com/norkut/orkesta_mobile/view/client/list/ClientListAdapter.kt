package com.norkut.orkesta_mobile.view.client.list

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.norkut.orkesta_mobile.common.utils.UI.Companion.setTextImageDrawable
import com.norkut.orkesta_mobile.databinding.ClientListItemBinding
import com.norkut.orkesta_mobile.domain.client.Client

class ClientListAdapter(private var clientList: MutableList<Client>,
                        private val onClick: (Client) -> Unit,
                        private val context: Context?) :  RecyclerView.Adapter<ClientListAdapter.ClientVH>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientVH =
        ClientVH(binding(parent))


    override fun onBindViewHolder(holder: ClientVH, position: Int) = with(holder) {
        val client = clientList.orderDesc()[position]
        bind(client)
        binding.root.setOnClickListener { onClick.invoke(client) }
    }

    override fun getItemCount(): Int = clientList.size

    private fun binding(parent: ViewGroup) =
        ClientListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)


    class ClientVH(val binding: ClientListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(value: Client) {
               with(binding){
                   tvDocument.text = String.format("%s - %s", value.documentIdentifier, value.documentNumber)
                   tvClientName.text = String.format("%s %s", value.firstName, value.lastName)
                   tvEmail.text = value.email
                   ivItemIcon.setTextImageDrawable(value.getFullName(), true)
               }
        }

    }



    @SuppressLint("NotifyDataSetChanged")
    fun swapData(newItems: MutableList<Client>) {
        clientList = newItems
        notifyDataSetChanged()
    }

    private fun List<Client>.orderDesc(): List<Client> {
        return sortedWith(compareBy({it.idClient}, {it.documentNumber}))
    }
}