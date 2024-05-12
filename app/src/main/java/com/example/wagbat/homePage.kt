package com.example.wagbat

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.google.firebase.database.*
import java.util.Locale

class homePage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var myResturantAdapter: MyResturantAdapter
    private var myResturantData: Array<MyResturantData> = emptyArray() // Initialize with an empty array

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference.child("restaurant")

        // Retrieve data from Firebase Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Toast.makeText(this@homePage, "Enter onDataChange", Toast.LENGTH_SHORT).show()

                // Clear existing data from MyResturantData array
                val list = ArrayList<MyResturantData>()
                for (snapshot in dataSnapshot.children) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    val photoUrl = snapshot.child("photo").getValue(String::class.java)
                    if (name != null && photoUrl != null) {
                        list.add(MyResturantData(name, photoUrl))
                    }
                }
                myResturantData = list.toTypedArray()
                for (restaurant in myResturantData) {
                    Toast.makeText(this@homePage, "Restaurant Name: ${restaurant.name}", Toast.LENGTH_SHORT).show()
                }
                myResturantAdapter = MyResturantAdapter(myResturantData, this@homePage)
                recyclerView.adapter = myResturantAdapter

                Toast.makeText(this@homePage, "Exit onDataChange", Toast.LENGTH_SHORT).show()

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
                if (i.name.lowercase(Locale.ROOT).contains(query)) {
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
}
