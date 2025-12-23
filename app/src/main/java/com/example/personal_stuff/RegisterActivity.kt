package com.example.personal_stuff

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personal_stuff.api.ApiClient
import com.example.personal_stuff.models.User
import com.example.personal_stuff.utils.SessionManager
import com.example.personal_stuff.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvGoToLogin: TextView
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        setupClickListeners()
    }

    private fun initializeViews() {
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etBirthDate = findViewById(R.id.etBirthDate)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvGoToLogin = findViewById(R.id.tvGoToLogin)
    }

    private fun setupClickListeners() {
        btnRegister.setOnClickListener { attemptRegister() }
        tvGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun attemptRegister() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val birthDate = etBirthDate.text.toString().trim()
        val password = etPassword.text.toString()

        if (name.isEmpty() || email.isEmpty() || birthDate.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(id = -1, name = name, email = email, birthDate = birthDate) // ID will be assigned by server

        apiClient.register(user, password) { response, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this, "Network Error: ${error.message}", Toast.LENGTH_LONG).show()
                } else if (response != null && response.success && response.user != null) {
                    sessionManager.saveUser(response.user)
                    Toast.makeText(this, response.message ?: "Registration successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ShopActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, response?.message ?: "Registration failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}