package com.example.personal_stuff.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_stuff.R
import com.example.personal_stuff.models.Order

class OrderAdapter(
    private val orderList: List<Order>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderIdTextView: TextView = itemView.findViewById(R.id.tvOrderId)
        private val orderDateTextView: TextView = itemView.findViewById(R.id.tvOrderDate)
        private val orderStatusTextView: TextView = itemView.findViewById(R.id.tvOrderStatus)
        private val orderTotalTextView: TextView = itemView.findViewById(R.id.tvOrderTotal)

        fun bind(order: Order) {
            orderIdTextView.text = "Order ID: ${order.id}"
            orderDateTextView.text = "Date: ${order.orderDate}"
            orderStatusTextView.text = "Status: ${order.status}"
            orderTotalTextView.text = "Total: \$${String.format("%.2f", order.totalPrice)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int = orderList.size
}