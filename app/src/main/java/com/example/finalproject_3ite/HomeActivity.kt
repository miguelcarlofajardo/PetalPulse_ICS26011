package com.example.finalproject_3ite

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        val homeButton : ImageButton = findViewById(R.id.btnHome)
        val shopButton : ImageButton = findViewById(R.id.btnShop)
        val userButton : ImageButton = findViewById(R.id.btnUser)

        userButton.setOnClickListener(){
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        shopButton.setOnClickListener(){
            val intent = Intent(this, HomeMainActivity::class.java)
            startActivity(intent)
        }
    }
}