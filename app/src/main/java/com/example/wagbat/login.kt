package com.example.wagbat

import UserData
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class login : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        database = FirebaseDatabase.getInstance().reference.child("Users")

        var email = findViewById<EditText>(R.id.email)
        var password = findViewById<EditText>(R.id.password)
        var loginBtn = findViewById<Button>(R.id.loginBtn)
        var goSignup = findViewById<Button>(R.id.goSignup)

        goSignup.setOnClickListener {
            var intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            var emailTxt = email.text.toString()
            var passwordTxt = password.text.toString()

            database.orderByChild("email").equalTo(emailTxt).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val user = userSnapshot.getValue(UserData::class.java)
                            if (user != null && user.password == passwordTxt) {
                                Toast.makeText(this@login, "Logged in successfully", LENGTH_LONG).show()
                                val intent = Intent(this@login, homePage::class.java)
                                intent.putExtra("user", user)
                                startActivity(intent)
                                return
                            }
                        }
                        Toast.makeText(this@login, "Invalid email or password", LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@login, "User not found", LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@login, "Database error: ${databaseError.message}", LENGTH_LONG).show()
                }
            })
        }
    }
}
