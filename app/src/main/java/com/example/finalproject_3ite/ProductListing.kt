package com.example.finalproject_3ite

import ProductClass
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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

        val productName: EditText = findViewById(R.id.edtEditName)
        val productSize: EditText = findViewById(R.id.edtEditUsername)
        val productPrice: EditText = findViewById(R.id.edtEditOldPassword)
        val productDescription: EditText = findViewById(R.id.edtEditEmail)
        val productImage: Button = findViewById(R.id.btnUploadImage)
        val publishButton: Button = findViewById(R.id.btnPublish)
        val homeButton: ImageButton = findViewById(R.id.btnHome)
        val shopButton: ImageButton = findViewById(R.id.btnShop)

        shopButton.setOnClickListener {
            val intent = Intent(this, HomeMainActivity::class.java)
            startActivity(intent)
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

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
        // Create and show a ProgressDialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Access the TextView and update the text
        val textViewMessage = dialog.findViewById<TextView>(R.id.textViewMessage)
        textViewMessage.text = "Uploading..."

        // Show the dialog
        dialog.show()


        // Generate a unique image name using UUID
        val imageName = UUID.randomUUID().toString()

        // Create a reference to the storage path for the image
        val imageRef = storageReference.child("images/$imageName")

        // Upload the image to Firebase Storage
        val uploadTask = imageRef.putFile(imageUri)

        // Set up a listener to be notified when the upload is complete
        uploadTask.addOnCompleteListener { task ->
            // Dismiss the ProgressDialog
            dialog.dismiss()
            if (task.isSuccessful) {
                // If the image upload is successful, get the download URL
                imageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    if (urlTask.isSuccessful) {
                        // Extract the download URL from the task result
                        val imageUrl = urlTask.result.toString()

                        // Generate a unique productId for the product
                        val dataKey = databaseReference.push().key

                        // Create a ProductClass object with the product details
                        val products = ProductClass(
                            dataKey.toString(), name, price, size, description, imageUrl)

                        databaseReference.child(dataKey.toString()).setValue(products)

                        // Pass productId along with other details to ProductDetailsActivity
                        val intent = Intent(this@ProductListing, ProductDetailsActivity::class.java)
                        intent.putExtra("productId", dataKey)
                        intent.putExtra("productName", name)
                        intent.putExtra("productPrice", price.toString())
                        intent.putExtra("productSize", size)
                        intent.putExtra("productDescription", description)
                        intent.putExtra("imageUrl", imageUrl)
                        startActivity(intent)
                    } else {
                        // Handle the case where getting the image URL fails
                        Log.e("ProductListing", "Failed to get image URL", urlTask.exception)
                        Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Handle the case where the image upload fails
                Log.e("ProductListing", "Failed to upload image", task.exception)
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }



    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
