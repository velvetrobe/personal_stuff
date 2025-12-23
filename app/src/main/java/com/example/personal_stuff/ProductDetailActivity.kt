package com.example.personal_stuff

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personal_stuff.api.ApiClient
import com.example.personal_stuff.models.Product
import com.example.personal_stuff.utils.SessionManager
import com.example.personal_stuff.R
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var ivProductDetail: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvPrice: TextView
    private lateinit var btnBack: Button
    private lateinit var btnAddToCart: Button
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private var userId: Int = -1
    private var productId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        sessionManager = SessionManager(this)
        val user = sessionManager.getUser()
        if (user == null) {
            finish()
            return
        }
        userId = user.id

        productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId == -1) {
            finish()
            return
        }

        initializeViews()
        apiClient = ApiClient()

        loadProductDetails()
        setupClickListeners()
    }

    private fun initializeViews() {
        ivProductDetail = findViewById(R.id.ivProductDetail)
        tvProductName = findViewById(R.id.tvProductName)
        tvCategory = findViewById(R.id.tvCategory)
        tvDescription = findViewById(R.id.tvDescription)
        tvPrice = findViewById(R.id.tvPrice)
        btnBack = findViewById(R.id.btnBack)
        btnAddToCart = findViewById(R.id.btnAddToCart)
    }

    private fun loadProductDetails() {
        apiClient.getProductById(productId) { product, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this, "Error loading product: ${error.message}", Toast.LENGTH_SHORT).show()
                    finish() // Or handle error differently
                } else if (product != null) {
                    displayProduct(product)
                } else {
                    Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
                    finish() // Or handle error differently
                }
            }
        }
    }

    private fun displayProduct(product: Product) {
        tvProductName.text = product.name
        tvCategory.text = product.category
        tvDescription.text = product.description
        tvPrice.text = "$${product.price}"

        Picasso.get()
            .load(product.imageUrl)
            .placeholder(R.drawable.placeholder_vinyl) // Add a placeholder image
            .error(R.drawable.error_vinyl)           // Add an error image
            .into(ivProductDetail)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener { finish() }
        btnAddToCart.setOnClickListener { addToCart() }
    }

    private fun addToCart() {
        apiClient.addToCart(userId, productId, 1) { message, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this, "Error adding to cart: ${error.message}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, message ?: "Added to cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}