package com.example.finalproject_3ite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        auth = FirebaseAuth.getInstance()

        val imageView = findViewById<ImageView>(R.id.imgLogo)
        imageView.bringToFront()

        val textViewCreateAccount = findViewById<TextView>(R.id.txtCreate)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etEmail = findViewById<EditText>(R.id.edtEmail)
        val etPassword = findViewById<EditText>(R.id.edtPassword)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                // Handle empty fields
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        textViewCreateAccount.setOnClickListener {
            // to register
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login success
                    val user: FirebaseUser? = auth.currentUser
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    // to home main activity
                    val intent = Intent(this, HomeMainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // wrong sign in exceptions
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        // invalid email or password
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    } catch (e: FirebaseAuthInvalidUserException) {
                        // user not found exception
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        //others
                        Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
