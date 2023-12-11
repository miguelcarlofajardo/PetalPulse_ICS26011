package com.example.finalproject_3ite

import UserProfile
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtNewPassword: EditText
    private lateinit var btnUpdate: Button

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_page)

        // Initialize views
        edtName = findViewById(R.id.edtEditName)
        edtUsername = findViewById(R.id.edtEditUsername)
        edtPassword = findViewById(R.id.edtEditOldPassword)
        edtEmail = findViewById(R.id.edtEditEmail)
        edtNewPassword = findViewById(R.id.edtEditNewPassword)
        btnUpdate = findViewById(R.id.btnRPublish)

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId!!)

        // Fetch user data from the database
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserProfile::class.java)

                    // Populate EditTexts with current user data
                    edtName.setText(user?.name)
                    edtUsername.setText(user?.username)
                    edtEmail.setText(user?.email)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        // Update user data when the "Update" button is clicked
        btnUpdate.setOnClickListener {
            updateUserProfile()
        }
    }

    private fun updateUserProfile() {
        val name = edtName.text.toString().trim()
        val username = edtUsername.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val newPassword = edtNewPassword.text.toString().trim()

        // Validate input fields
        if (name.isEmpty()) {
            edtName.error = "Name is required"
            edtName.requestFocus()
            return
        }

        if (username.isEmpty()) {
            edtUsername.error = "Username is required"
            edtUsername.requestFocus()
            return
        }

        if (email.isEmpty()) {
            edtEmail.error = "Email is required"
            edtEmail.requestFocus()
            return
        }

        // Prompt user to enter old password
        val oldPassword = edtPassword.text.toString().trim()

        if (oldPassword.isEmpty()) {
            showAlertDialog("Old Password Required", "Please enter your old password.")
            return
        }

        // Reauthenticate user before updating profile
        val user = auth.currentUser
        val credentials = user?.email?.let { EmailAuthProvider.getCredential(it, oldPassword) }

        if (user != null && credentials != null) {
            user.reauthenticate(credentials)
                .addOnSuccessListener {
                    // Reauthentication successful, update user profile
                    updateProfileAndNavigate(name, username, email, newPassword)
                }
                .addOnFailureListener {
                    // Reauthentication failed
                    showAlertDialog("Authentication Failed", "Please enter the correct old password.")
                }
        }
    }

    private fun updateProfileAndNavigate(name: String, username: String, email: String, newPassword: String) {
        // Update user data in the database
        val user = UserProfile(name, username, email)

        // Update the user data in Firebase
        databaseReference.setValue(user)
            .addOnSuccessListener {
                // Update successful

                // If a new password is provided, update the password in FirebaseAuth
                if (newPassword.isNotEmpty()) {
                    updatePassword(newPassword)
                } else {
                    // If no new password is provided, navigate back to the profile page
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            .addOnFailureListener {
                // Update failed
                showAlertDialog("Update Failed", "Failed to update profile. Please enter valid inputs.")
            }
    }

    private fun updatePassword(newPassword: String) {
        val user = auth.currentUser

        user?.updatePassword(newPassword)
            ?.addOnSuccessListener {
                // Password update successful, redirect to profile page
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
            ?.addOnFailureListener {
                // Password update failed
                showAlertDialog("Password Update Failed", "Failed to update password. Please enter a valid password.")
            }
    }

    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }
}
