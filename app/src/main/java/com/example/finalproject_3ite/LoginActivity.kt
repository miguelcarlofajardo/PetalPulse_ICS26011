package com.example.finalproject_3ite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val imageView = findViewById<ImageView>(R.id.imgLogo)
        imageView.bringToFront()

        val textViewCreateAccount = findViewById<TextView>(R.id.txtCreate)
        val buttonLogin : Button = findViewById(R.id.btnLogin)

        textViewCreateAccount.setOnClickListener {
            // Handle click event, e.g., navigate to the registration activity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener{
            val intent = Intent(this, HomeMainActivity::class.java)
            startActivity(intent)
        }

    }
}
