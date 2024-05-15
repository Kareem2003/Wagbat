package com.example.wagbat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CartAdapter(private val cartItems: List<CartItem>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartImageView: ImageView = itemView.findViewById(R.id.cartImageview)
        val dishNameTextView: TextView = itemView.findViewById(R.id.dishName)
        val priceTextView: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_list, parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartItems[position]

        // Set dish name and price
        holder.dishNameTextView.text = currentItem.dishName
        holder.priceTextView.text = currentItem.price.toString()

        // Load image using Picasso
        Picasso.get().load(currentItem.dishImage).into(holder.cartImageView)
    }

    override fun getItemCount() = cartItems.size
}
