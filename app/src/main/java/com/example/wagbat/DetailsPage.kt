package com.example.wagbat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class DetailsPage : AppCompatActivity() {

    private lateinit var dishesRecyclerView: RecyclerView
    private lateinit var dishAdapter: DishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details_page)

        // Retrieve data from intent extras
        val restaurantId = intent.getStringExtra("id")
        intent.putExtra("resid", restaurantId)
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("restaurant").child(
            restaurantId.toString()
        )

        // Set restaurant name and photo
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.child("name").getValue(String::class.java)
                val photoUrl = dataSnapshot.child("photo").getValue(String::class.java)

                val nameTextView: TextView = findViewById(R.id.restaurantName)
                nameTextView.text = name

                val photoImageView: ImageView = findViewById(R.id.imageview)
                Picasso.get().load(photoUrl).into(photoImageView)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })

        // Initialize RecyclerView for dishes
        dishesRecyclerView = findViewById(R.id.dishesRecyclerView)
        dishAdapter = DishAdapter()
        dishAdapter.setOnDishClickListener(object : DishAdapter.OnDishClickListener {
            override fun onDishClick(dish: DishData) {
                // Start DetailsPage activity with the selected restaurant's data
                val intent = Intent(this@DetailsPage, DishDetails::class.java)
                intent.putExtra("dishId", dish.ID)
                intent.putExtra("dishName", dish.name)
                intent.putExtra("photoUrl", dish.photoUrl)
                intent.putExtra("price", dish.price)
                startActivity(intent)
            }
        })
        dishesRecyclerView.adapter = dishAdapter
        dishesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch menu data from Firebase
        val menuRef = database.child("menu")
        menuRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dishesList = ArrayList<DishData>()
                for (snapshot in dataSnapshot.children) {
                    val dishId = snapshot.child("id").getValue(String::class.java)
                    val dishName = snapshot.child("dish").getValue(String::class.java)
                    val dishPhotoUrl = snapshot.child("photo").getValue(String::class.java)
                    val dishPrice = snapshot.child("price").getValue(Double::class.java)
                    if (dishId != null && dishName != null && dishPhotoUrl != null && dishPrice != null) {
                        val dish = DishData(dishId, dishName, dishPhotoUrl, dishPrice)
                        dishesList.add(dish)
                    }
                }
                dishAdapter.setDishList(dishesList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }
}
