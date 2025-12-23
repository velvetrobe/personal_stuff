package com.example.personal_stuff

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_stuff.adapters.ProductAdapter
import com.example.personal_stuff.api.ApiClient
import com.example.personal_stuff.utils.SessionManager
import com.example.personal_stuff.R

class ShopActivity : AppCompatActivity() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private var userId: Int = -1

    // UI elements for user info and navigation
    private lateinit var tvUserName: TextView
    private lateinit var btnProfile: Button
    private lateinit var btnCart: Button
    private lateinit var btnOrders: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        sessionManager = SessionManager(this)
        val user = sessionManager.getUser()
        if (user == null) {
            // Should not happen if this activity is only reachable after login
            finish()
            return
        }
        userId = user.id

        initializeViews()
        apiClient = ApiClient()

        loadProducts()

        // Display user info
        tvUserName.text = "Welcome, ${user.name}!"
        btnProfile.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }
        btnCart.setOnClickListener { startActivity(Intent(this, CartActivity::class.java)) }
        btnOrders.setOnClickListener { startActivity(Intent(this, OrderActivity::class.java)) }
    }

    private fun initializeViews() {
        rvProducts = findViewById(R.id.rvProducts)
        tvUserName = findViewById(R.id.tvUserName)
        btnProfile = findViewById(R.id.btnProfile)
        btnCart = findViewById(R.id.btnCart)
        btnOrders = findViewById(R.id.btnOrders)

        rvProducts.layoutManager = LinearLayoutManager(this)
    }

    private fun loadProducts() {
        apiClient.getAllProducts { products, error ->
            runOnUiThread {
                if (error != null) {
                    // Handle error (e.g., show a message)
                    Log.e("ShopActivity", "Error loading products: ${error.message}")
                } else if (products != null) {
                    adapter = ProductAdapter(products) { product ->
                        val intent = Intent(this, ProductDetailActivity::class.java)
                        intent.putExtra("PRODUCT_ID", product.id)
                        startActivity(intent)
                    }
                    rvProducts.adapter = adapter
                }
            }
        }
    }
}