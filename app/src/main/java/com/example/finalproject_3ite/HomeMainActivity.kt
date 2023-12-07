package com.example.finalproject_3ite

import ProductClass
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        val productsRef = database.child("products")

        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userArrayList.clear()

                    for (productSnapshot in snapshot.children) {
                        val productId = productSnapshot.key ?: ""
                        val name = productSnapshot.child("productName").getValue(String::class.java) ?: ""
                        val price = productSnapshot.child("productPrice").getValue(Long::class.java)?.toString() ?: ""
                        val size = productSnapshot.child("productSize").getValue(String::class.java) ?: ""
                        val description = productSnapshot.child("productDescription").getValue(String::class.java) ?: ""
                        val imageUrl = productSnapshot.child("imageUrl").getValue(String::class.java) ?: ""

                        Log.d("FirebaseData", "ID: $productId, Name: $name, Price: $price, Size: $size, Description: $description, ImageUrl: $imageUrl")

                        val imageId = R.drawable.flowersample

                        val product = ProductClass(productId, name, price, size, description, imageUrl)
                        userArrayList.add(product)
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
