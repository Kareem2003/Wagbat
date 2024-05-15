package com.example.wagbat

import UserData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfilePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)

        // Initialize views
        val userNameTextView: TextView = findViewById(R.id.userName)
        val emailTextView: TextView = findViewById(R.id.email)
        val signOutButton: Button = findViewById(R.id.signOutButton)
        val historyButton: Button = findViewById(R.id.historyBtn)

        // Get user ID from SharedPreferences
        val userId = getUserId(this)

        // Retrieve user information from database using user ID
        if (userId != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(UserData::class.java)
                        if (user != null) {
                            userNameTextView.text = "${user.fullName}"
                            emailTextView.text = "${user.email}"
                        } else {
                            // Handle case where user object is null
                        }
                    } else {
                        // Handle case where user node does not exist
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                    Toast.makeText(this@ProfilePage, "Database error: ${databaseError.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        // Implement sign out functionality
        signOutButton.setOnClickListener {
            // Clear user ID from SharedPreferences
            clearUserId(this)
            // Redirect to login page
            startActivity(Intent(this, login::class.java))
            finish()
        }

        historyButton.setOnClickListener{
            val intent = Intent(this, HistoryPage::class.java)
            startActivity(intent)
        }
    }


    // Function to retrieve user ID from SharedPreferences
    private fun getUserId(context: Context): String? {
        val sharedPref = context.getSharedPreferences("com.example.wagbat.USER_ID_PREFS", Context.MODE_PRIVATE)
        return sharedPref.getString("userId", null)
    }

    // Function to clear user ID from SharedPreferences
    private fun clearUserId(context: Context) {
        val sharedPref = context.getSharedPreferences("com.example.wagbat.USER_ID_PREFS", Context.MODE_PRIVATE)
        sharedPref.edit().remove("userId").apply()
    }
}
