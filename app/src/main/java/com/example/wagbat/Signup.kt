package com.example.wagbat

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

class Signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        auth = Firebase.auth
        var email = findViewById<EditText>(R.id.email)
        var password = findViewById<EditText>(R.id.password)
        var signupBtn = findViewById<Button>(R.id.signupBtn)

        signupBtn.setOnClickListener{
            var emailTxt = email.text.toString()
            var passwordTxt = password.text.toString()
            auth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Signed up successfully", Toast.LENGTH_LONG).show()

                        var intent = Intent(this, login::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Faild", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}