package com.example.wagbat

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import java.util.*

class homePage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var MyResturantAdapter: MyResturantAdapter
    private var MyResturantData = ArrayList<MyResturantData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<MyResturantData>()
            for (i in MyResturantData) {
                if (i.ResturantName.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                MyResturantAdapter.setfilterlist(filteredList)
            }
        }
    }
    }
