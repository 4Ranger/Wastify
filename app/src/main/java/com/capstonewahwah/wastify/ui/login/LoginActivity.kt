package com.capstonewahwah.wastify.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
            if (binding.edtEmail.text.toString().trim().isEmpty()) {
                binding.edtEmail.error = "Silahkan masukkan username anda terlebih dahulu"
            } else if (binding.edtPassword.text.toString().trim().isEmpty()) {
                binding.edtPassword.error = "Silahkan masukkan password anda terlebih dahulu"
            } else {
                // handle login
                loginViewModel.login(
                    binding.edtEmail.text.toString().trim(),
                    binding.edtPassword.text.toString().trim()
                )

                loginViewModel.login.observe(this) { user ->
                    if (!user.error!!) {
                        val session = UserModel(
                            userId = user.loginResult?.uid!!,
                            username = user.loginResult.username!!,
                            token = user.loginResult.token!!,
                            email = user.loginResult.email!!,
                            history = user.loginResult.historyCount!!,
                            points = user.loginResult.historyPoints!!,
                            isLoggedIn = true
                        )
                        loginViewModel.saveSession(session)
                    }
                }
            }
        }

        loginViewModel.getSession().observe(this) { user ->
            if (user.isLoggedIn) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

        loginViewModel.authLoading.observe(this) { isLoading ->
            setLoading(isLoading)
        }

        loginViewModel.response.observe(this) { response ->
            Toast.makeText(this, "${response.code} - ${response.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnLogin.text = ""
            binding.loader.visibility = View.VISIBLE
        } else {
            binding.btnLogin.text = getString(R.string.login)
            binding.loader.visibility = View.GONE
        }
    }
}