package com.example.wagbat

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle

class homePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val MyResturantData = arrayOf(
            MyResturantData("Karam El Sham" , R.drawable.img),
            MyResturantData("El Khedewy" , R.drawable.img1),
            MyResturantData("TUT's" ,  R.drawable.img2),
            MyResturantData("Pizza King" ,  R.drawable.img3),
            MyResturantData("Buffalo Burger" , R.drawable.img4),
            MyResturantData("Koshry El Tahrir" ,  R.drawable.img5),
            MyResturantData("Bazooka" ,  R.drawable.img6),
            MyResturantData("Gad" , R.drawable.img7),
            MyResturantData("Rosto" ,  R.drawable.img8),
            MyResturantData("Dar El Yemen" , R.drawable.img9),
            MyResturantData("Heart Attack" , R.drawable.img10)
        )

        val MyResturantAdapter = MyResturantAdapter(MyResturantData, this)
        recyclerView.adapter = MyResturantAdapter
    }
}