package com.example.finalproject_3ite

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.productdetails_page)

        databaseReference = FirebaseDatabase.getInstance().getReference("products")

        val backButton: ImageButton = findViewById(R.id.btnBack)
        val orderButton: Button = findViewById(R.id.btnOrder)
        val productImageView: ImageView = findViewById(R.id.productImageView)
        val productNameTextView: TextView = findViewById(R.id.productName)
        val productPriceTextView: TextView = findViewById(R.id.productPrice)
        val productSizeTextView: TextView = findViewById(R.id.productSize)
        val productDescriptionTextView: TextView = findViewById(R.id.productDescription)

        // Retrieve data from the Intent
        val productId = intent.getStringExtra("productId")
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getFloatExtra("productPrice", 0.0f)
        val productSize = intent.getStringExtra("productSize")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Fetch product description from Firebase
        databaseReference.child(productId ?: "").child("productDescription")
            .get()
            .addOnSuccessListener { snapshot ->
                val productDescription = snapshot.value as? String
                Log.d("ProductDetailsActivity", "Product Description (Firebase): $productDescription")

                // Set data to views
                Picasso.get().load(imageUrl).into(productImageView)
                productNameTextView.text = productName
                productPriceTextView.text = productPrice.toString()
                productSizeTextView.text = productSize
                productDescriptionTextView.text = productDescription

                // Logging the product description
                Log.d("ProductDetailsActivity", "Product Description: $productDescription")
            }
            .addOnFailureListener { e ->
                Log.e("ProductDetailsActivity", "Error fetching product description", e)
            }

        backButton.setOnClickListener {
            onBackPressed()
        }

        orderButton.setOnClickListener {
            showSuccessDialog(productId)
            Log.d("ProductDetailsActivity", "Deleting product with ID: $productId")
            deleteData(productId)
        }
    }

    private fun showSuccessDialog(productId: String?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.success_dialog)

        val doneButton: Button = dialog.findViewById(R.id.btnDone)
        doneButton.setOnClickListener {
            // Delete the data and redirect to HomeMainActivity
            deleteData(productId)
            redirectToHomeMainActivity()

            // Dismiss the dialog when the "Done" button is clicked
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteData(productId: String?) {
        // Implement your logic to delete data using productId
        if (productId != null) {
            databaseReference.child(productId).removeValue()
        }
    }

    private fun redirectToHomeMainActivity() {
        // Implement logic to start HomeMainActivity
        val intent = Intent(this, HomeMainActivity::class.java)
        startActivity(intent)
    }
}
