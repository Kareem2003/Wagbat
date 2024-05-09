package com.example.wagbat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        var email = findViewById<EditText>(R.id.email)
        var password = findViewById<EditText>(R.id.password)
        var loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener{
            var emailTxt = email.text.toString()
            var passwordTxt = password.text.toString()
            auth.signInWithEmailAndPassword(emailTxt, passwordTxt)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Loged in successfully", LENGTH_LONG).show()
                        val user = auth.currentUser
                        var intent = Intent(this, homePage::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "FAILD", LENGTH_LONG).show()
                    }
                }
        }
    }
}