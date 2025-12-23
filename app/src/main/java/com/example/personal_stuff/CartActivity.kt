package com.example.personal_stuff

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_stuff.adapters.CartItemAdapter
import com.example.personal_stuff.api.ApiClient
import com.example.personal_stuff.models.CartItem
import com.example.personal_stuff.models.Product
import com.example.personal_stuff.utils.SessionManager
import com.example.personal_stuff.R

class CartActivity : AppCompatActivity() {

    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnCheckout: Button
    private lateinit var btnBack: Button
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private var userId: Int = -1
    private var cartItems: List<CartItem> = emptyList()
    private var allProducts: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        sessionManager = SessionManager(this)
        val user = sessionManager.getUser()
        if (user == null) {
            finish()
            return
        }
        userId = user.id

        initializeViews()
        apiClient = ApiClient()

        loadCart()
        setupClickListeners()
    }

    private fun initializeViews() {
        rvCartItems = findViewById(R.id.rvCartItems)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        btnCheckout = findViewById(R.id.btnCheckout)
        btnBack = findViewById(R.id.btnBack)

        rvCartItems.layoutManager = LinearLayoutManager(this)
    }

    private fun loadCart() {
        // First, get the cart items
        apiClient.getCart(userId) { items, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this, "Error loading cart: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@runOnUiThread
                }
                cartItems = items ?: emptyList()

                // Then, get all products to match details
                apiClient.getAllProducts { products, productError ->
                    runOnUiThread {
                        if (productError != null) {
                            Toast.makeText(this, "Error loading products: ${productError.message}", Toast.LENGTH_SHORT).show()
                            return@runOnUiThread
                        }
                        allProducts = products ?: emptyList()
                        updateCartView()
                    }
                }
            }
        }
    }

    private fun updateCartView() {
        // Calculate totals
        var totalPrice = 0.0
        var totalQuantity = 0
        cartItems.forEach { cartItem ->
            val product = allProducts.find { it.id == cartItem.productId }
            if (product != null) {
                totalPrice += product.price * cartItem.quantity
                totalQuantity += cartItem.quantity
            }
        }
        tvTotalPrice.text = "Total: \$${String.format("%.2f", totalPrice)}"

        // Update RecyclerView adapter
        val adapter = CartItemAdapter(cartItems, allProducts,
            onQuantityChanged = { productId, newQuantity ->
                apiClient.updateQuantity(userId, productId, newQuantity) { message, error ->
                    runOnUiThread {
                        if (error != null) {
                            Toast.makeText(this, "Error updating quantity: ${error.message}", Toast.LENGTH_SHORT).show()
                        } else {
                            // Reload the cart to reflect changes
                            loadCart()
                        }
                    }
                }
            },
            onItemRemoved = { productId ->
                apiClient.removeFromCart(userId, productId) { message, error ->
                    runOnUiThread {
                        if (error != null) {
                            Toast.makeText(this, "Error removing item: ${error.message}", Toast.LENGTH_SHORT).show()
                        } else {
                            // Reload the cart to reflect changes
                            loadCart()
                        }
                    }
                }
            }
        )
        rvCartItems.adapter = adapter
    }


    private fun setupClickListeners() {
        btnBack.setOnClickListener { finish() }
        btnCheckout.setOnClickListener { checkout() }
    }

    private fun checkout() {
        apiClient.checkout(userId) { response, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this, "Error during checkout: ${error.message}", Toast.LENGTH_LONG).show()
                } else if (response != null && response.success) {
                    Toast.makeText(this, "Checkout successful! Order ID: ${response.orderId}", Toast.LENGTH_LONG).show()
                    // Optionally, clear the cart UI or refresh the activity
                    finish() // Go back to previous screen (e.g., ShopActivity)
                } else {
                    Toast.makeText(this, response?.message ?: "Checkout failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}