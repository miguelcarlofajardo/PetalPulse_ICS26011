package com.example.finalproject_3ite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)

        val buttonAdd = findViewById<Button>(R.id.btnAddItem)

        buttonAdd.setOnClickListener{
            val intent = Intent(this, ProductListing::class.java)
            startActivity(intent)
        }

    }
}