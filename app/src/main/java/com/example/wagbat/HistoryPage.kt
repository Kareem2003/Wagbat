package com.example.wagbat

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryPage : AppCompatActivity() {
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyItems: MutableList<HistoryData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hestory_page)

        // Initialize RecyclerView and HistoryAdapter
        val historyRecyclerView: RecyclerView = findViewById(R.id.historyRecyclerView)
        historyItems = mutableListOf()
        historyAdapter = HistoryAdapter(historyItems)
        historyRecyclerView.adapter = historyAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve user ID
        val userId = getUserId(this)

        // Retrieve history items from the database using user ID
        userId?.let {
            val historyRef = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(it) // Using the retrieved user ID
                .child("history")

            historyRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    historyItems.clear() // Clear previous items
                    for (historySnapshot in dataSnapshot.children) {
                        val historyItem = historySnapshot.getValue(HistoryData::class.java)
                        historyItem?.let {
                            historyItems.add(historyItem)
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
