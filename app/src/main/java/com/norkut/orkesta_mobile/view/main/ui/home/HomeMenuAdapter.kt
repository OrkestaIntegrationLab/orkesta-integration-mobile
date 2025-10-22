package com.norkut.orkesta_mobile.view.main.ui.home

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.norkut.orkesta_mobile.databinding.ItemHomeMenuBinding

class HomeMenuAdapter(private var menuItems: List<Pair<Int, Int>>,
                      private val onClick: (Int) -> Unit,
                      private val navMenuItems: Menu?) : Adapter<HomeMenuAdapter.MenuVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuVH =
        MenuVH(binding(parent))


    private fun binding(parent: ViewGroup) =
        ItemHomeMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun onBindViewHolder(holder: MenuVH, position: Int) = with(holder) {
        val menuItem = menuItems[position]
        bind(menuItem.first, menuItem.second, navMenuItems)
        binding.root.setOnClickListener { onClick.invoke(menuItem.first) }

    }


    override fun getItemCount(): Int = menuItems.size

    class MenuVH(val binding: ItemHomeMenuBinding): ViewHolder(binding.root) {
        fun bind(
            menuItemId: Int,
            menuItemIcon: Int,
            navMenuItems: Menu?
        ) {
            with(binding) {
                tvItemMenuName.text = navMenuItems?.findItem(menuItemId)?.title
                ibItemMenuIcon.setImageResource(menuItemIcon)
            }
        }
    }
}