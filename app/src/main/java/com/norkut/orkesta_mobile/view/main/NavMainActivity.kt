package com.norkut.orkesta_mobile.view.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.norkut.orkesta_mobile.R
import com.norkut.orkesta_mobile.databinding.ActivityMainBinding
import com.norkut.orkesta_mobile.view.client.list.ClientListFragment
import com.norkut.orkesta_mobile.view.main.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NavMainActivity : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       binding = ActivityMainBinding.inflate(layoutInflater)
       binding.setupViews()
       this.title = getString(R.string.app_name)
   }

    private fun ActivityMainBinding.setupViews() {
        setContentView(root)
        setSupportActionBar(appBarNavMain.toolbar)
        setupDrawerAction()
        navView.setNavigationItemSelectedListener(this@NavMainActivity)
        navView.setCheckedItem(R.id.nav_home)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_about -> showAboutDialog()
            else -> {
                val fragment = buildSelectedFragment(item)
                supportFragmentManager.beginTransaction().replace(R.id.content_fragment, fragment).commit()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        return true
    }

    private fun buildSelectedFragment(item: MenuItem): Fragment {
        return when (item.itemId) {
            R.id.nav_home -> HomeFragment()
            R.id.nav_client -> ClientListFragment()
            else -> HomeFragment()
        }
    }

    private fun showAboutDialog() {
        AboutDialog(this, layoutInflater).apply {
            setVersion()
            //setConfigInfo(viewModel.configResponse.value)
            //setPositiveButton(R.string.dialog_close, null)
            create()
        }.show()
    }

    private fun setupDrawerAction() {
        val toggle = ActionBarDrawerToggle(
            this@NavMainActivity,
            binding.drawerLayout,
            binding.appBarNavMain.toolbar,
            R.string.open_navigation_drawer,
            R.string.close_navigation_drawer
        )
        toggle.syncState()
        binding.drawerLayout.addDrawerListener(toggle)
    }



}