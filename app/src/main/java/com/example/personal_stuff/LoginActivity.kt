package com.example.personal_stuff

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personal_stuff.api.ApiClient
import com.example.personal_stuff.utils.SessionManager
import com.example.personal_stuff.R

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvGoToRegister: TextView
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, ShopActivity::class.java))
            finish()
            return
        }

        initializeViews()
        apiClient = ApiClient()

        setupClickListeners()
    }

    private fun initializeViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvGoToRegister = findViewById(R.id.tvGoToRegister)
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener { attemptLogin() }
        tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun attemptLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        apiClient.login(email, password) { response, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this, "Network Error: ${error.message}", Toast.LENGTH_LONG).show()
                } else if (response != null && response.success && response.user != null) {
                    sessionManager.saveUser(response.user)
                    Toast.makeText(this, response.message ?: "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ShopActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, response?.message ?: "Login failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}