package com.fari.agrovision.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fari.agrovision.R
import com.fari.agrovision.databinding.ActivityMainMenuBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setToolBar()
        setBottomNav()

    }

    private fun setBottomNav() {
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        val bottomNavController = findNavController(R.id.bottom_nav_host_fragment)
        bottomNavController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.tvToolbarTitle.text = destination.label
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_history,
                R.id.navigation_detection,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(bottomNavController,appBarConfiguration)

        bottomNavView.setupWithNavController(bottomNavController)
    }

    private fun setToolBar() {
        val toolbar: Toolbar = binding.toolbar.toolbar
        setSupportActionBar(toolbar)

        binding.toolbar.btnBackToolbar.visibility = View.INVISIBLE
    }
}