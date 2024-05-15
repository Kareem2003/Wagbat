package com.example.wagbat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Calendar

class CartPage : AppCompatActivity() {

    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartItems: MutableList<CartItem>
    private lateinit var placeOrderBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart_page)

        placeOrderBtn = findViewById(R.id.placeOrder)

        // Initialize RecyclerView and CartAdapter
        val cartRecyclerView: RecyclerView = findViewById(R.id.cartRecyclerView)
        cartItems = mutableListOf()
        cartAdapter = CartAdapter(cartItems)
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve user ID from SharedPreferences
        val userId = getUserId(this)

        // Retrieve cart items from the database using user ID
        if (userId != null) {
            val userCartRef = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(userId)
                .child("cart")

            userCartRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    cartItems.clear() // Clear previous items
                    for (cartSnapshot in dataSnapshot.children) {
                        val cartItem = cartSnapshot.getValue(CartItem::class.java)
                        if (cartItem != null) {
                            cartItems.add(cartItem)
                        }
                    }
                    cartAdapter.notifyDataSetChanged() // Notify adapter of data change
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })

            // Enable or disable place order button based on current time
            val currentTime = Calendar.getInstance()
            val hourOfDay = 11

            val isOrderBeforeNoon = hourOfDay < 10
            val isOrderBefore3PM = hourOfDay < 13

            if (isOrderBeforeNoon || isOrderBefore3PM) {
                placeOrderBtn.isEnabled = true
                placeOrderBtn.setOnClickListener {
                    // Handle place order button click
                    placeOrder(userId)
                    // Display the Done Fragment Page
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val doneFragment = DoneFragment()
                    fragmentTransaction.replace(R.id.fragmentContainer, doneFragment)
                    fragmentTransaction.commit()
                }
            } else {
                placeOrderBtn.isEnabled = false
                placeOrderBtn.setOnClickListener {
                    // Handle disabled button click (optional)
                    Toast.makeText(this@CartPage, "Order placement window closed.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Function to get the current user's ID from SharedPreferences
    private fun getUserId(context: Context): String? {
        val sharedPref = context.getSharedPreferences("com.example.wagbat.USER_ID_PREFS", Context.MODE_PRIVATE)
        return sharedPref.getString("userId", null)
    }

    // Function to place order and move cart items to history
    private fun placeOrder(userId: String) {

        val cartRef = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)
            .child("cart")

        val historyRef = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)
            .child("history")

        cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Move cart items to history
                    for (cartSnapshot in dataSnapshot.children) {
                        val cartItem = cartSnapshot.getValue(CartItem::class.java)
                        if (cartItem != null) {
                            historyRef.push().setValue(cartItem)
                        }
                    }
                    // Clear cart after moving items to history
                    cartRef.removeValue()
                    Toast.makeText(this@CartPage, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CartPage, "Cart is empty!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(this@CartPage, "Failed to place order: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
