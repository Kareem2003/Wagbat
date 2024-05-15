package com.example.wagbat
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import android.content.Context
import android.content.SharedPreferences

class DishDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_details)

        // Retrieve dish details from intent extras
        val dishId = intent.getStringExtra("dishId")
        val dishName = intent.getStringExtra("dishName")
        val dishPrice = intent.getDoubleExtra("price", 0.0)
        val dishPhotoUrl = intent.getStringExtra("photoUrl")

        // Initialize views
        val dishNameTextView: TextView = findViewById(R.id.DishDetailName)
        val dishPriceTextView: TextView = findViewById(R.id.DishDetailprice)
        val dishPhotoImageView: ImageView = findViewById(R.id.imageDishview)
        val addToCartButton: Button = findViewById(R.id.addToCartButton)

        // Set dish details to views
        dishNameTextView.text = dishName
        dishPriceTextView.text = "Price: $dishPrice"
        Picasso.get().load(dishPhotoUrl).into(dishPhotoImageView)

        // Implement add to cart button functionality if needed
        addToCartButton.setOnClickListener {
            // Get the currently logged-in user's ID
            val userId = getUserId(this)

            // Check if the user is logged in
            if (userId != null) {
                // Construct a reference to the user's cart in the database
                val userCartRef = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(userId)
                    .child("cart")

                // Generate a new unique key for the dish entry in the cart
                val cartItemId = userCartRef.push().key

                // Create a HashMap to hold the dish details
                val dishDetails = HashMap<String, Any>()
                dishDetails["dishId"] = dishId ?: ""
                dishDetails["dishName"] = dishName ?: ""
                dishDetails["dishImage"] = dishPhotoUrl ?: ""
                dishDetails["price"] = dishPrice

                // Add the dish details to the user's cart
                if (cartItemId != null) {
                    userCartRef.child(cartItemId).setValue(dishDetails)
                        .addOnSuccessListener {
                            // Cart item added successfully
                            Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            // Failed to add item to cart
                            Toast.makeText(this, "Failed to add item to cart: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Failed to generate cart item ID
                    Toast.makeText(this, "Failed to generate cart item ID", Toast.LENGTH_SHORT).show()
                }
            } else {
                // User is not logged in
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to get the current user's ID from SharedPreferences
    private fun getUserId(context: Context): String? {
        // Get the SharedPreferences instance
        val sharedPref: SharedPreferences = context.getSharedPreferences("com.example.wagbat.USER_ID_PREFS", Context.MODE_PRIVATE)

        // Retrieve the user ID from SharedPreferences
        return sharedPref.getString("userId", "")
    }
}
