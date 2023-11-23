package com.example.finalproject_3ite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        auth = FirebaseAuth.getInstance()

        val imageView = findViewById<ImageView>(R.id.imgLogo2)
        imageView.bringToFront()
        val textViewCreateAccount = findViewById<TextView>(R.id.txtLoginHere)

        val btnRegister = findViewById<Button>(R.id.btnRegister_1)
        val etName = findViewById<EditText>(R.id.edtName)
        val etUsername = findViewById<EditText>(R.id.edtUsername)
        val etEmail = findViewById<EditText>(R.id.edtEmail2)
        val etPassword = findViewById<EditText>(R.id.edtPassword2)

        textViewCreateAccount.setOnClickListener {
            // Handle click event, e.g., navigate to the login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (name.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(name, username, email, password)
            } else {
                // Handle empty fields
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(name: String, username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration success
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If registration fails, handle different exceptions
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        // Handle weak password exception
                        Toast.makeText(this, "Weak password", Toast.LENGTH_SHORT).show()
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        // Handle invalid email exception
                        Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                    } catch (e: FirebaseAuthUserCollisionException) {
                        // Handle user already exists exception
                        Toast.makeText(this, "User with this email already exists", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        // Log the exception for further analysis
                        Log.e("RegistrationError", "Registration failed", e)
                        // Display a generic error message to the user
                        Toast.makeText(this, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
