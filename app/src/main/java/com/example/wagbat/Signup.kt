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
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        // define database functions
        auth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        database = firebaseDatabase.reference.child("Users")

        var email = findViewById<EditText>(R.id.email)
        var fullName = findViewById<EditText>(R.id.FullNAme)
        var password = findViewById<EditText>(R.id.password)
        var signupBtn = findViewById<Button>(R.id.signupBtn)
        var goLogin = findViewById<Button>(R.id.goLogin)

        goLogin.setOnClickListener {
            var intent = Intent(this, login::class.java)
            startActivity(intent)
        }

        signupBtn.setOnClickListener{
            var fullnameTxt = fullName.text.toString()
            var emailTxt = email.text.toString()
            var passwordTxt = password.text.toString()

            if (fullnameTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            this@Signup.database.orderByChild("email").equalTo(emailTxt).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (!datasnapshot.exists()) {
                        val id = this@Signup.database.push().key
                        val userData = UserData(id, fullnameTxt, emailTxt, passwordTxt)
                        this@Signup.database.child(id!!).setValue(userData)
                        Toast.makeText(this@Signup, "Signup successfully", Toast.LENGTH_LONG).show()
                        val login_page = Intent(this@Signup, login::class.java)
                        startActivity(login_page)
                        finish()
                    } else {
                        // Handle the case where the email already exists
                        Toast.makeText(this@Signup, "Email already exists", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Empty implementation or handle errors as needed
                }
            })
        }
    }
}