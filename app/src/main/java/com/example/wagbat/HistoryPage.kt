package com.example.wagbat

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class HistoryPage : AppCompatActivity() {

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyData: MutableList<HistoryData>
    private lateinit var placeOrderBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hestory_page)

//        placeOrderBtn = findViewById(R.id.placeOrderButton)

        // Initialize RecyclerView and HistoryAdapter
        val historyRecyclerView: RecyclerView = findViewById(R.id.historyRecyclerView)
        historyData = mutableListOf()
        historyAdapter = HistoryAdapter(historyData)
        historyRecyclerView.adapter = historyAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve user ID from SharedPreferences
        val userId = getUserId(this)

        // Retrieve history items from the database using user ID
        if (userId != null) {
            val userHistoryRef = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(userId)
                .child("history")

            userHistoryRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    historyData.clear() // Clear previous items
                    for (historySnapshot in dataSnapshot.children) {
                        val historyItem = historySnapshot.getValue(HistoryData::class.java)
                        if (historyItem != null) {
                            historyData.add(historyItem)
                        }
                    }
                    historyAdapter.notifyDataSetChanged() // Notify adapter of data change
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    // Function to get the current user's ID from SharedPreferences
    private fun getUserId(context: Context): String? {
        val sharedPref = context.getSharedPreferences("com.example.wagbat.USER_ID_PREFS", Context.MODE_PRIVATE)
        return sharedPref.getString("userId", null)
    }
}
