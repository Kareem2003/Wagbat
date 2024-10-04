package com.example.wagbat

import UserData
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class Signup : AppCompatActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        firebaseDatabase = FirebaseDatabase.getInstance()
        database = firebaseDatabase.reference.child("Users")

        // Find views
        val emailEditText = findViewById<EditText>(R.id.email)
        val fullNameEditText = findViewById<EditText>(R.id.FullNAme)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val signupBtn = findViewById<Button>(R.id.signupBtn)
        val goLoginBtn = findViewById<Button>(R.id.goLogin)

        goLoginBtn.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }

        signupBtn.setOnClickListener{
            val fullName = fullNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validate fields
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if email already exists
            database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // Email does not exist, create new user
                        val userId = database.push().key!!
                        val userData = UserData(userId, fullName, email, password)
                        database.child(userId).setValue(userData)
                        Toast.makeText(this@Signup, "Signup successful", Toast.LENGTH_LONG).show()
                        val loginIntent = Intent(this@Signup, login::class.java)
                        startActivity(loginIntent)
                        finish()
                    } else {
                        // Email already exists
                        Toast.makeText(this@Signup, "Email already exists", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                    Toast.makeText(this@Signup, "Database error: ${databaseError.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
