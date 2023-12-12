package com.example.finalproject_3ite

import ProductClass
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject_3ite.databinding.ActivityListmainBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListmainBinding
    private lateinit var userArrayList: ArrayList<ProductClass>
    private lateinit var database: DatabaseReference
    private lateinit var originalProductList: List<ProductClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListmainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ArrayList
        userArrayList = ArrayList()

        // Initialize Firebase
        database = Firebase.database.reference

        // Retrieve data from Firebase and populate the ArrayList
        readDataFromFirebase()

        // Set up the ListView using the binding object
        binding.listView.adapter = AdapterClass(this, userArrayList)

        val userButton: ImageButton = findViewById(R.id.btnUser)
        val homeButton: ImageButton = findViewById(R.id.btnHome)
        val searchButton: ImageButton = findViewById(R.id.btnSearch)

        searchButton.setOnClickListener {
            val searchEditText = binding.etSearchQuery

            // If the EditText is visible, perform the search
            if (searchEditText.visibility == View.VISIBLE) {
                val searchQuery = searchEditText.text.toString().toLowerCase()

                if (searchQuery.isBlank()) {
                    // If the search query is blank, restore the original list
                    (binding.listView.adapter as? AdapterClass)?.restoreOriginalList()
                } else {
                    val filteredList = userArrayList.filter {
                        it.productName.toLowerCase().contains(searchQuery) ||
                                it.productSize.toLowerCase().contains(searchQuery) ||
                                it.productDescription.toLowerCase().contains(searchQuery)
                    }.toMutableList()

                    (binding.listView.adapter as? AdapterClass)?.updateList(filteredList)
                }
            }

            // Toggle the visibility of the EditText
            searchEditText.visibility = if (searchEditText.visibility == View.VISIBLE) {
                View.GONE
            } else {
                searchEditText.requestFocus()
                View.VISIBLE
            }
        }

        userButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun readDataFromFirebase() {
        val productsRef = database.child("products") // Make sure it matches your Firebase structure

        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    originalProductList = userArrayList.toList()
                    userArrayList.clear()

                    for (productSnapshot in snapshot.children) {
                        val productId = productSnapshot.key ?: ""
                        val productName =
                            productSnapshot.child("productName").getValue(String::class.java) ?: ""
                        val productPrice =
                            productSnapshot.child("productPrice").getValue(Float::class.java) ?: 0.0f
                        val productSize =
                            productSnapshot.child("productSize").getValue(String::class.java) ?: ""
                        val description =
                            productSnapshot.child("productDescription").getValue(String::class.java)
                                ?: ""
                        val imageUrl =
                            productSnapshot.child("imageUrl").getValue(String::class.java) ?: ""

                        Log.d(
                            "FirebaseData",
                            "ID: $productId, Name: $productName, Price: $productPrice, Size: $productSize, Description: $description, ImageUrl: $imageUrl"
                        )

                        val products = ProductClass(
                            productId, productName, productPrice, productSize, description, imageUrl
                        )
                        userArrayList.add(products)
                    }

                    (binding.listView.adapter as AdapterClass).notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeMainActivity", "Error reading data from Firebase: ${error.message}")
            }
        })
    }
}