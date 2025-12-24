package com.example.personal_stuff.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_stuff.R
import com.example.personal_stuff.adapters.OrderAdapter
import com.example.personal_stuff.api.ApiClient
import com.example.personal_stuff.models.Order
import com.example.personal_stuff.utils.SessionManager

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
            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if no user is logged in
            return
        }
        userId = user.id

        initializeViews()
        apiClient = ApiClient()

        loadOrders() // Fetch orders from the API
        setupClickListeners()
    }

    private fun initializeViews() {
        rvOrders = findViewById(R.id.rvOrders)
        btnBack = findViewById(R.id.btnBack)

        rvOrders.layoutManager = LinearLayoutManager(this)
        // rvOrders.adapter will be set after loading orders
    }

    private fun loadOrders() {
        apiClient.getUserOrders(userId) { ordersList, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this, "Error loading orders: ${error.message}", Toast.LENGTH_LONG).show()
                    // Optionally, handle error differently, e.g., show an empty state or error message in the RecyclerView
                } else if (ordersList != null) {
                    orders = ordersList
                    updateOrderList() // Update the RecyclerView with the fetched orders
                } else {
                    // API returned success but no data (shouldn't happen with current API, but good practice)
                    Toast.makeText(this, "No orders found.", Toast.LENGTH_SHORT).show()
                    orders = emptyList()
                    updateOrderList()
                }
            }
        }
    }

    private fun updateOrderList() {
        if (orders.isEmpty()) {

        }
        val adapter = OrderAdapter(orders)
        rvOrders.adapter = adapter
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener { finish() }
    }
}