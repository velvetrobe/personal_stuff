package com.example.personal_stuff

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personal_stuff.utils.SessionManager
import com.example.personal_stuff.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var tvProfileBirthDate: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnBack: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeViews()
        sessionManager = SessionManager(this)

        displayUserInfo()
        setupClickListeners()
    }

    private fun initializeViews() {
        tvProfileName = findViewById(R.id.tvProfileName)
        tvProfileEmail = findViewById(R.id.tvProfileEmail)
        tvProfileBirthDate = findViewById(R.id.tvProfileBirthDate)
        btnLogout = findViewById(R.id.btnLogout)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun displayUserInfo() {
        val user = sessionManager.getUser()
        if (user != null) {
            tvProfileName.text = user.name
            tvProfileEmail.text = user.email
            tvProfileBirthDate.text = user.birthDate
        } else {
            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
            finish() // Or redirect to login
        }
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener { finish() }
        btnLogout.setOnClickListener {
            sessionManager.clearUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}