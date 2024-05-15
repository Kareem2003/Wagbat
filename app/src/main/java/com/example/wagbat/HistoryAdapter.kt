package com.example.wagbat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class HistoryAdapter(private val historyItems: List<HistoryData>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item_list, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyItems[position]
        holder.dishNameTextView.text = currentItem.dishName
        holder.priceTextView.text = "$${currentItem.price}"
        Picasso.get().load(currentItem.imageUrl).into(holder.imageView)
    }

    override fun getItemCount() = historyItems.size

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishNameTextView: TextView = itemView.findViewById(R.id.historyDishName)
        val priceTextView: TextView = itemView.findViewById(R.id.historyPrice)
        val imageView: ImageView = itemView.findViewById(R.id.historyImageview)
    }
}
