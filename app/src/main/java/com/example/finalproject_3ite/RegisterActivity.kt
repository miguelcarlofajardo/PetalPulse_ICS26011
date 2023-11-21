package com.example.finalproject_3ite

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val imageView = findViewById<ImageView>(R.id.imgLogo2)
        imageView.bringToFront()
        val textViewCreateAccount = findViewById<TextView>(R.id.txtLoginHere)

        textViewCreateAccount.setOnClickListener {
            // Handle click event, e.g., navigate to the registration activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
