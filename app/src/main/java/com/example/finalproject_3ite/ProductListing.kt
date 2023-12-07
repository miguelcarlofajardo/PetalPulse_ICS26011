package com.example.finalproject_3ite

import ProductClass
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class ProductListing : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_page)

        databaseReference = FirebaseDatabase.getInstance().getReference("products")
        storageReference = FirebaseStorage.getInstance().reference

        val productName: EditText = findViewById(R.id.edtProductName)
        val productSize: EditText = findViewById(R.id.edtSize)
        val productPrice: EditText = findViewById(R.id.edtPrice)
        val productDescription: EditText = findViewById(R.id.edtProductDescription)
        val productImage: Button = findViewById(R.id.btnUploadImage)
        val publishButton: Button = findViewById(R.id.btnPublish)

        productImage.setOnClickListener {
            openGallery()
        }

        publishButton.setOnClickListener {
            val name = productName.text.toString()
            val size = productSize.text.toString()
            val price = productPrice.text.toString().toFloat()
            val description = productDescription.text.toString()

            if (this::imageUri.isInitialized) {
                // Show a loading indicator here (optional)
                uploadImage(name, size, price, description)

                productName.setText("")
                productSize.setText("")
                productPrice.setText("")
                productDescription.setText("")
                val drawable = ContextCompat.getDrawable(this, R.drawable.flowersample)
                findViewById<ImageView>(R.id.imageView).setImageDrawable(drawable)

            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            findViewById<ImageView>(R.id.imageView).setImageURI(imageUri)
        }
    }

    private fun uploadImage(name: String, size: String, price: Float, description: String) {
        val imageName = UUID.randomUUID().toString()
        val imageRef = storageReference.child("images/$imageName")

        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnCompleteListener { task ->
            // Hide the loading indicator here (optional)

            if (task.isSuccessful) {
                imageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    if (urlTask.isSuccessful) {
                        val imageUrl = urlTask.result.toString()

                        // Get the generated productId
                        val dataKey = databaseReference.push().key

                        val product = ProductClass(
                            dataKey.toString(), name, price.toString(), size, description, imageUrl)

                        // Pass productId along with other details to ProductDetailsActivity
                        val intent = Intent(this@ProductListing, ProductDetailsActivity::class.java)
                        intent.putExtra("productId", dataKey)
                        intent.putExtra("productName", name)
                        intent.putExtra("productPrice", price.toString())
                        intent.putExtra("productDescription", description)
                        intent.putExtra("imageUrl", imageUrl)
                        startActivity(intent)

                    } else {
                        Log.e("ProductListing", "Failed to get image URL", urlTask.exception)
                        Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Log.e("ProductListing", "Failed to upload image", task.exception)
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
