package com.capstonewahwah.wastify.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.data.local.pref.UserModel
import com.capstonewahwah.wastify.databinding.ActivityLoginBinding
import com.capstonewahwah.wastify.helper.ViewModelFactory
import com.capstonewahwah.wastify.ui.main.MainActivity
import com.capstonewahwah.wastify.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvToRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            if (binding.edtUsername.text.toString().trim().isEmpty()) {
                binding.edtUsername.error = "Silahkan masukkan username anda terlebih dahulu"
            } else if (binding.edtPassword.text.toString().trim().isEmpty()) {
                binding.edtPassword.error = "Silahkan masukkan password anda terlebih dahulu"
            } else {
                // handle login
                loginViewModel.saveSession(
                    UserModel(
                        "1212",
                        binding.edtUsername.text.toString().trim(),
                        "12312312",
                        true
                    )
                )
            }
        }

        loginViewModel.getSession().observe(this) { user ->
            if (user.isLoggedIn) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}