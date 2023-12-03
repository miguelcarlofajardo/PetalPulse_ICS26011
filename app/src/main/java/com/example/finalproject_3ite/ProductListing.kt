package com.example.finalproject_3ite

import ProductClass
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProductListing : AppCompatActivity() {

    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_page)

        databaseReference = FirebaseDatabase.getInstance().getReference("products")

        val productName: EditText = findViewById(R.id.edtProductName)
        val productSize: EditText = findViewById(R.id.edtSize)
        val productPrice: EditText = findViewById(R.id.edtPrice)
        val productDescription: EditText = findViewById(R.id.edtProductDescription)
        val productImage: ImageButton = findViewById(R.id.uploadImage)

        // Your Publish button
        val publishButton: Button = findViewById(R.id.btnPublish)

        publishButton.setOnClickListener {
            // Get the values from EditText fields
            val name = productName.text.toString()
            val size = productSize.text.toString()
            val price = productPrice.text.toString().toFloat()
            val description = productDescription.text.toString()

            // Save data to Firebase
            val product = ProductClass(name, size, price, description)

            val dataKey = databaseReference.push().key
            databaseReference.child(dataKey!!).setValue(product).addOnSuccessListener {
                Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

