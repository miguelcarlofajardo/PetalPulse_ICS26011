package com.example.finalproject_3ite

import UserProfile
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)

        val addButton : Button = findViewById(R.id.btnAddItem)

        addButton.setOnClickListener{
            val intent = Intent(this, ProductListing::class.java)
            startActivity(intent)
        }

        // Retrieve user ID from FirebaseAuth
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Fetch user data from the database
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId!!)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserProfile::class.java)

                    // Display data in EditText fields
                    val editTextName = findViewById<EditText>(R.id.edtName4)
                    val editTextUsername = findViewById<EditText>(R.id.edtUsername7)
                    val editTextEmail = findViewById<EditText>(R.id.edtEmail5)

                    editTextName.setText(user?.name)
                    editTextUsername.setText(user?.username)
                    editTextEmail.setText(user?.email)

                    editTextName.isEnabled = false
                    editTextUsername.isEnabled = false
                    editTextEmail.isEnabled = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}