package com.example.finalproject_3ite

import UserProfile
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var txtNameProfile: TextView
    private lateinit var txtUsernameProfile: TextView
    private lateinit var txtEmailProfile: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)

        val addButton: Button = findViewById(R.id.btnAddItem)

        txtNameProfile = findViewById(R.id.txtNameProfile)
        txtUsernameProfile = findViewById(R.id.txtUsernameProfile)
        txtEmailProfile = findViewById(R.id.txtUsernameEmail)

        addButton.setOnClickListener {
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

                    // Display data in TextViews
                    txtNameProfile.text = user?.name
                    txtUsernameProfile.text = user?.username
                    txtEmailProfile.text = user?.email
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
