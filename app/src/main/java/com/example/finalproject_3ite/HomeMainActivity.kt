package com.example.finalproject_3ite

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
    private lateinit var userArrayList: ArrayList<Flowers>
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

//        // Inside onCreate()
//// Comment out or remove readDataFromFirebase() and the Firebase-related code
//
//// Sample data for testing
//        val sampleImageId = R.drawable.flowersample
//        val sampleName = "Sample Flower"
//        val samplePrice = "100"
//        val sampleRating = "4.0"
//
//        userArrayList.add(Flowers(sampleName, samplePrice, sampleRating, "", sampleImageId))


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
        val productsRef = database.child("products") // Change "flowers" to "products"

        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userArrayList.clear()

                    for (productSnapshot in snapshot.children) {
                        val name = productSnapshot.child("productName").getValue(String::class.java) ?: ""
                        val price = productSnapshot.child("productPrice").getValue(Long::class.java)?.toString() ?: ""
                        val rating = productSnapshot.child("productSize").getValue(String::class.java) ?: ""
                        val imageUrl = productSnapshot.child("imageUrl").getValue(String::class.java) ?: ""

                        Log.d("FirebaseData", "Name: $name, Price: $price, Rating: $rating, ImageUrl: $imageUrl")

                        val imageId = R.drawable.flowersample

                        val product = Flowers(name, price, rating, imageUrl, imageId)
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
