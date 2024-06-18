package com.capstonewahwah.wastify.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.databinding.ActivityWelcomeBinding
import com.capstonewahwah.wastify.ui.login.LoginActivity
import com.capstonewahwah.wastify.ui.main.MainActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLoginWastify.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
        }

        binding.btnLoginGoogle.setOnClickListener {
            Toast.makeText(this@WelcomeActivity, "Sedang dalam pengerjaan..", Toast.LENGTH_SHORT).show()
        }
    }
}