package com.example.wagbat

import UserData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class login : AppCompatActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        firebaseDatabase = FirebaseDatabase.getInstance()
        database = firebaseDatabase.reference.child("Users")

        // Find views
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val goSignupBtn = findViewById<Button>(R.id.goSignup)

        goSignupBtn.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if email exists
            database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Email exists, verify password
                        for (userSnapshot in dataSnapshot.children) {
                            val user = userSnapshot.getValue(UserData::class.java)
                            if (user != null && user.password == password) {
                                // Password is correct, log in
                                Toast.makeText(this@login, "Logged in successfully", Toast.LENGTH_SHORT).show()
                                saveUserIdToSharedPreferences(user.id)
                                val homeIntent = Intent(this@login, homePage::class.java)
                                homeIntent.putExtra("user", user)
                                startActivity(homeIntent)
                                finish()
                                return
                            }
                        }
                        // Incorrect password
                        Toast.makeText(this@login, "Invalid email or password", Toast.LENGTH_LONG).show()
                    } else {
                        // Email does not exist
                        Toast.makeText(this@login, "User not found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                    Toast.makeText(this@login, "Database error: ${databaseError.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun saveUserIdToSharedPreferences(userId: String?) {
        val sharedPref = getSharedPreferences("com.example.wagbat.USER_ID_PREFS", Context.MODE_PRIVATE)
        sharedPref.edit().putString("userId", userId ?: "").apply()
    }

}
