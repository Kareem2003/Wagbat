package com.example.wagbat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class HistoryAdapter(private val historyItems: List<HistoryData>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartImageView: ImageView = itemView.findViewById(R.id.historyImageview)
        val dishNameTextView: TextView = itemView.findViewById(R.id.dishName)
        val priceTextView: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item_list, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyItems[position]

        // Set dish name and price
        holder.dishNameTextView.text = currentItem.dishName
        holder.priceTextView.text = currentItem.price.toString()

        // Load image using Picasso
        Picasso.get().load(currentItem.dishImage).into(holder.cartImageView)
    }

    override fun getItemCount() = historyItems.size
}
