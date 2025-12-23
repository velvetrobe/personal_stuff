package com.example.personal_stuff

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_stuff.api.ApiClient
import com.example.personal_stuff.models.Order
import com.example.personal_stuff.utils.SessionManager
import com.example.personal_stuff.R

class OrderActivity : AppCompatActivity() {

    private lateinit var rvOrders: RecyclerView
    private lateinit var btnBack: Button
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private var userId: Int = -1
    private var orders: List<Order> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        sessionManager = SessionManager(this)
        val user = sessionManager.getUser()
        if (user == null) {
            finish()
            return
        }
        userId = user.id

        initializeViews()
        apiClient = ApiClient()

        loadOrders()
        setupClickListeners()
    }

    private fun initializeViews() {
        rvOrders = findViewById(R.id.rvOrders)
        btnBack = findViewById(R.id.btnBack)

        rvOrders.layoutManager = LinearLayoutManager(this)
        // rvOrders.adapter = OrderAdapter(orders) // Implement OrderAdapter if needed
    }

    private fun loadOrders() {
        // Placeholder: In a real app, you would have an API endpoint to get orders by user ID.
        // For now, show a message or implement a mock list if needed.
        Toast.makeText(this, "Order history functionality would be implemented here.", Toast.LENGTH_LONG).show()
        // Example mock data if needed:
        // orders = listOf(Order(...), Order(...))
        // rvOrders.adapter = OrderAdapter(orders)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener { finish() }
    }
}