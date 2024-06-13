package com.capstonewahwah.wastify.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.databinding.ActivityRegisterBinding
import com.capstonewahwah.wastify.helper.ViewModelFactory
import com.capstonewahwah.wastify.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvToLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        binding.btnRegister.setOnClickListener {
            registerViewModel.register(
                binding.edtUsername.text.toString().trim(),
                binding.edtEmail.text.toString().trim(),
                binding.passwordEditText.text.toString().trim()
            )
        }

        registerViewModel.register.observe(this) { response ->
            Log.d("Register", response.toString())
        }
    }
}