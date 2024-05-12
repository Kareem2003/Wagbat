package com.example.wagbat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CartAdapter (private var myResturantData: Array<MyResturantData>, private val context: Context):
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ResturantImage: ImageView = itemView.findViewById(R.id.imageview)
        var textViewName: TextView = itemView.findViewById(R.id.textName)
    }
    fun setfilterlist(myResturantData: List<MyResturantData>){
        this.myResturantData = myResturantData.toTypedArray()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.resturant_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myResturantDataList = myResturantData[position]
        holder.textViewName.text = myResturantDataList.name
        Picasso.get().load(myResturantDataList.photoUrl).into(holder.ResturantImage)

        holder.itemView.setOnClickListener {
            Toast.makeText(context, myResturantDataList.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return myResturantData.size
    }
}