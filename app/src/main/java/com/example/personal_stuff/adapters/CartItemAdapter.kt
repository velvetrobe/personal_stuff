package com.example.personal_stuff.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_stuff.models.CartItem
import com.example.personal_stuff.models.Product
import com.example.personal_stuff.R
import com.squareup.picasso.Picasso

class CartItemAdapter(
    private val cartItems: List<CartItem>,
    private val allProducts: List<Product>,
    private val onQuantityChanged: (Int, Int) -> Unit, // productId, newQuantity
    private val onItemRemoved: (Int) -> Unit          // productId
) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvCartProductName)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvCartProductPrice)
        private val quantityTextView: TextView = itemView.findViewById(R.id.tvCartQuantity)
        private val imageView: ImageView = itemView.findViewById(R.id.ivCartProductImage)
        private val btnIncrease: Button = itemView.findViewById(R.id.btnIncrease)
        private val btnDecrease: Button = itemView.findViewById(R.id.btnDecrease)
        private val btnRemove: Button = itemView.findViewById(R.id.btnRemove)

        fun bind(cartItem: CartItem) {
            val product = allProducts.find { it.id == cartItem.productId }
            if (product != null) {
                nameTextView.text = product.name
                priceTextView.text = "$${product.price}"
                quantityTextView.text = cartItem.quantity.toString()

                Picasso.get()
                    .load(product.imageUrl)
                    .placeholder(R.drawable.placeholder_vinyl) // Add a placeholder image
                    .error(R.drawable.error_vinyl)           // Add an error image
                    .into(imageView)

                btnIncrease.setOnClickListener {
                    val newQuantity = cartItem.quantity + 1
                    onQuantityChanged(cartItem.productId, newQuantity)
                }

                btnDecrease.setOnClickListener {
                    val newQuantity = if (cartItem.quantity > 1) cartItem.quantity - 1 else 0
                    if (newQuantity == 0) {
                        onItemRemoved(cartItem.productId) // Remove item if quantity becomes 0
                    } else {
                        onQuantityChanged(cartItem.productId, newQuantity)
                    }
                }

                btnRemove.setOnClickListener {
                    onItemRemoved(cartItem.productId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size
}