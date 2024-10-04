package com.example.wagbat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*

class homePage : AppCompatActivity(), MyResturantAdapter.OnRestaurantClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var goToCartPage: Button
    private lateinit var goToProfilePage: Button
    private lateinit var myResturantAdapter: MyResturantAdapter
    private var myResturantData: Array<MyResturantData> = emptyArray() // Initialize with an empty array

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        goToCartPage = findViewById(R.id.goToCartPage)
        goToProfilePage = findViewById(R.id.goToProfilePage)

        goToCartPage.setOnClickListener{
            val intent = Intent(this@homePage, CartPage::class.java)
            startActivity(intent)
        }

        goToProfilePage.setOnClickListener{
            val intent = Intent(this@homePage, ProfilePage::class.java)
            startActivity(intent)
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference.child("restaurant")

        // Retrieve data from Firebase Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear existing data from MyResturantData array
                val list = ArrayList<MyResturantData>()
                for (snapshot in dataSnapshot.children) {
                    val ID = snapshot.child("id").getValue(String::class.java)
                    val name = snapshot.child("name").getValue(String::class.java)
                    val photoUrl = snapshot.child("photo").getValue(String::class.java)
                    if (name != null && photoUrl != null) {
                        list.add(MyResturantData(ID, name, photoUrl))
                    }
                }
                myResturantData = list.toTypedArray()
                myResturantAdapter = MyResturantAdapter(myResturantData, this@homePage)
                myResturantAdapter.setOnRestaurantClickListener(this@homePage)
                recyclerView.adapter = myResturantAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@homePage, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<MyResturantData>()
            for (i in myResturantData) {
                if (i.name.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                myResturantAdapter.setfilterlist(filteredList)
            }
        }
    }

    // Click listener implementation
    override fun onRestaurantClick(restaurant: MyResturantData) {
        // Start DetailsPage activity with the selected restaurant's data
        val intent = Intent(this, DetailsPage::class.java)
        intent.putExtra("id", restaurant.ID)
        intent.putExtra("name", restaurant.name)
        intent.putExtra("photoUrl", restaurant.photoUrl)
        startActivity(intent)
    }
}
