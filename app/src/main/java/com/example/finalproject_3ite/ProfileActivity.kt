package com.example.finalproject_3ite

import UserProfile
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
        val editButton: Button = findViewById(R.id.btnEditAccount)
        val deleteButton: Button = findViewById(R.id.btnDeleteAccount)
        val logoutButton: ImageButton = findViewById(R.id.btnLogout)

        txtNameProfile = findViewById(R.id.txtNameProfile)
        txtUsernameProfile = findViewById(R.id.txtUsernameProfile)
        txtEmailProfile = findViewById(R.id.txtUsernameEmail)
        var buttonHome = findViewById<ImageButton>(R.id.btnHome)
        var buttonShop = findViewById<ImageButton>(R.id.btnShop)

        buttonHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        buttonShop.setOnClickListener {
            val intent = Intent(this, HomeMainActivity::class.java)
            startActivity(intent)
        }
        addButton.setOnClickListener {
            val intent = Intent(this, ProductListing::class.java)
            startActivity(intent)
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            // Show AlertDialog to confirm account deletion
            showDeleteConfirmationDialog()
        }

        logoutButton.setOnClickListener {
            // Show AlertDialog to confirm logout
            showLogoutConfirmationDialog()
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
                Log.e("ProfileActivity", "Error reading data from Firebase: ${error.message}")
            }
        })
    }

    private fun showDeleteConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirm Deletion")
        alertDialogBuilder.setMessage("Are you sure you want to delete your account?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // User clicked Yes, proceed with account deletion
            deleteAccount()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            // User clicked No, dismiss the dialog
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }

    private fun showLogoutConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirm Logout")
        alertDialogBuilder.setMessage("Are you sure you want to log out?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // User clicked Yes, proceed with logout
            logout()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            // User clicked No, dismiss the dialog
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }

    private fun deleteAccount() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Account deleted successfully, remove user data from the database
                val userId = user.uid
                val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)

                databaseReference.removeValue().addOnCompleteListener { databaseTask ->
                    if (databaseTask.isSuccessful) {
                        // User data removed successfully, navigate to the login screen
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        // Failed to remove user data
                        Log.e("ProfileActivity", "Error removing user data: ${databaseTask.exception?.message}")
                    }
                }
            } else {
                // Failed to delete account
                Log.e("ProfileActivity", "Error deleting account: ${task.exception?.message}")
            }
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()

        // Navigate to the login screen
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
