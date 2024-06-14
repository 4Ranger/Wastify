package com.capstonewahwah.wastify.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
            // TODO: make the string below to string resources
            if (binding.edtUsername.text.toString().trim().isEmpty()) {
                binding.edtUsername.error = "Silahkan masukkan username anda terlebih dahulu"
            } else if (binding.edtEmail.text.toString().trim().isEmpty()) {
                binding.edtEmail.error = "Silahkan masukkan email anda terlebih dahulu"
            } else if (binding.passwordEditText.text.toString().trim().isEmpty()) {
                binding.passwordEditText.error = "Silahkan masukkan password anda terlebih dahulu"
            } else if (binding.passwordEditText.text?.length!! < 8) {
                binding.passwordEditText.error = "Password tidak boleh kurang dari 8 karakter"
            } else if (binding.konfirmasiPasswordEditText.text.toString().trim().isEmpty()) {
                binding.konfirmasiPasswordEditText.error = "Silahkan masukkan password anda sekali lagi"
            } else if (binding.passwordEditText.text.toString().trim() != binding.konfirmasiPasswordEditText.text.toString().trim()) {
                binding.passwordEditText.error = "Password anda tidak sama"
                binding.konfirmasiPasswordEditText.error = "Password anda tidak sama"
            } else {
                registerViewModel.register(
                    binding.edtUsername.text.toString().trim(),
                    binding.edtEmail.text.toString().trim(),
                    binding.passwordEditText.text.toString().trim()
                )
            }
        }

        registerViewModel.register.observe(this) { response ->
            if (response.message == "User registered successfully") {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
                Toast.makeText(this@RegisterActivity, response.message, Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.authLoading.observe(this) { isLoading ->
            setLoading(isLoading)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnRegister.text = ""
            binding.loader.visibility = View.VISIBLE
        } else {
            binding.btnRegister.text = getString(R.string.register)
            binding.loader.visibility = View.GONE
        }
    }
}