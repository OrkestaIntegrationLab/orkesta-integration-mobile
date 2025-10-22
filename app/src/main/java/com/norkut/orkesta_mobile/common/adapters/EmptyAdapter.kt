package com.norkut.orkesta_mobile.common.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.norkut.orkesta_mobile.R

class EmptyAdapter(    private val message: String,
                       private var itemsCount: Int = 1,
                       private val icon: Int = R.drawable.menu_ic_info) : RecyclerView.Adapter<EmptyAdapter.HeaderViewHolder>() {


    class HeaderViewHolder(
        view: View,
        private val message: String,
        private var itemsCount: Int,
        private val icon: Int
    ) : RecyclerView.ViewHolder(view) {
        private val layout: ConstraintLayout = itemView.findViewById(R.id.empty_fragment_layout)
        private val tvMessage: TextView = itemView.findViewById(R.id.message)
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivMessageIcon)

        fun bind() {
            setupView()
            tvMessage.text = message
            ivIcon.setImageResource(icon)
        }

        private fun setupView() {
            if (itemsCount == 0) {
                layout.visibility = View.VISIBLE
                layout.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            } else {
                layout.visibility = View.GONE
                layout.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val layout = R.layout.fragment_empty
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return HeaderViewHolder(view, message, itemsCount, icon)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = if (itemsCount > 0) 0 else 1

    @Deprecated(message = "utilizar hide()")
    @SuppressLint("NotifyDataSetChanged")
    fun updateItemsCount(itemsCount: Int) {
        this.itemsCount = itemsCount
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun hide() {
        this.itemsCount = 1
        notifyDataSetChanged()
    }


}