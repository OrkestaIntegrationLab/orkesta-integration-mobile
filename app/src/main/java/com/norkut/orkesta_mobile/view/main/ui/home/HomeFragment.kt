package com.norkut.orkesta_mobile.view.main.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import com.norkut.orkesta_mobile.R
import com.norkut.orkesta_mobile.common.adapters.EmptyAdapter
import com.norkut.orkesta_mobile.databinding.FragmentHomeBinding
import com.norkut.orkesta_mobile.view.client.list.ClientListFragment
import com.norkut.orkesta_mobile.view.main.NavMainViewModel
import kotlin.getValue


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: NavMainViewModel by activityViewModels()
    private var menuNavigationView: NavigationView? = null
    private lateinit var swipeContainer: SwipeRefreshLayout

    private lateinit var adapter: HomeMenuAdapter
    private lateinit var emptyAdapter: EmptyAdapter


    init {
        lifecycleScope.launchWhenStarted {
            activity?.title = getString(R.string.menu_home)
             setupAdapter()
            //setupSwipeContainer()
            //observe()
            setHasOptionsMenu(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeBinding.inflate(inflater, container, false)
              .root.also { binding = FragmentHomeBinding.bind(it) }


    private fun setupAdapter() {
        val onClick = { item: Int -> menuItemOnClick(item) }
        menuNavigationView = this.activity?.findViewById(R.id.nav_view)
        adapter = HomeMenuAdapter(viewModel.menu, onClick, menuNavigationView?.menu)
        emptyAdapter = EmptyAdapter(getString(R.string.no_associated_permissions_found), viewModel.menu.count())
        binding.rvHomeMenuItems.switchSpanCount(viewModel.menu.count())
        binding.rvHomeMenuItems.adapter = ConcatAdapter(adapter, emptyAdapter)
    }

    private fun menuItemOnClick(itemId: Int) {
        val fragment: Fragment= when (itemId) {
            R.id.nav_home -> HomeFragment()
            R.id.nav_client -> ClientListFragment()
            else -> HomeFragment()
        }
        this.activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.content_fragment, fragment)?.commit()
        menuNavigationView?.setCheckedItem(itemId)
    }

    private fun RecyclerView.switchSpanCount(items: Int) {
        val spanCount = if (items > 0) 2 else 1
        (this.layoutManager as GridLayoutManager).spanCount = spanCount
    }


}