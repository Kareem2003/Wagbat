package com.example.wagbat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DishAdapter : RecyclerView.Adapter<DishAdapter.ViewHolder>() {

    private var dishList: List<DishData> = ArrayList()

    // Define a click listener interface
    interface OnDishClickListener {
        fun onDishClick(dish: DishData)
    }

    // Initialize listener
    private lateinit var listener: OnDishClickListener

    // Setter method for listener
    fun setOnDishClickListener(listener: OnDishClickListener) {
        this.listener = listener
    }

    fun setDishList(dishList: List<DishData>) {
        this.dishList = dishList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dish_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishList[position]
        holder.nameTextView.text = dish.name
        holder.priceTextView.text = "Price: ${dish.price}"
        Picasso.get().load(dish.photoUrl).into(holder.imageView)

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            listener.onDishClick(dish)
        }
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.dishName)
        val priceTextView: TextView = itemView.findViewById(R.id.price)
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
    }
}
