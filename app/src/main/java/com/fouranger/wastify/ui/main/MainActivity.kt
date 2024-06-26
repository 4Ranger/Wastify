package com.fouranger.wastify.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fouranger.wastify.R
import com.fouranger.wastify.databinding.ActivityMainBinding
import com.fouranger.wastify.helper.ViewModelFactory
import com.fouranger.wastify.ui.welcome.WelcomeActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_profile, R.id.navigation_scan1, R.id.navigation_scan2 -> {
                    binding.cl.visibility = View.GONE
                }
                R.id.navigation_home -> binding.cl.visibility = View.VISIBLE
            }
        }

        binding.fab.setOnClickListener {
            navController.navigate(R.id.navigation_scan1)
        }

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLoggedIn) {
                startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                finish()
            } else {
                mainViewModel.getArticles()
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when (navController.currentDestination?.id) {
                R.id.navigation_home -> {
                    val customLayout = layoutInflater.inflate(R.layout.exit_dialog, null)
                    val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                        .setView(customLayout)

                    val yesBtn = customLayout.findViewById<MaterialButton>(R.id.btn_logout_yes)
                    val noBtn = customLayout.findViewById<MaterialButton>(R.id.btn_logout_no)

                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()

                    yesBtn.setOnClickListener {
                        mainViewModel.logout()
                        mainViewModel.deleteUserData()
                        alertDialog.dismiss()
                    }

                    noBtn.setOnClickListener {
                        alertDialog.dismiss()
                    }
                }
                R.id.navigation_scan2 -> {
                    navController.popBackStack(R.id.navigation_scan1,false)
                }
                R.id.navigation_preview -> {
                    navController.popBackStack(R.id.navigation_scan2, false)
                }
                R.id.navigation_result -> {
                    navController.popBackStack(R.id.navigation_preview, false)
                }
                R.id.navigation_details -> {
                    navController.popBackStack(R.id.navigation_classification, false)
                }
                else -> {
                    navController.popBackStack(R.id.navigation_home, false)
                    binding.cl.visibility = View.VISIBLE
                }
            }
        }
    }
}