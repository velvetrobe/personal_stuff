package com.example.personal_stuff.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_stuff.models.Product
import com.example.personal_stuff.R
import com.squareup.picasso.Picasso

class ProductAdapter(
    private val productList: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvProductName)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvProductPrice)
        private val imageView: ImageView = itemView.findViewById(R.id.ivProductImage)

        fun bind(product: Product) {
            nameTextView.text = product.name
            priceTextView.text = "$${product.price}"

            Picasso.get()
                .load(product.imageUrl)
                .placeholder(R.drawable.placeholder_vinyl)
                .error(R.drawable.error_vinyl)
                .into(imageView)

            itemView.setOnClickListener { onItemClick(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}