package com.example.finalproject_3ite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject_3ite.databinding.ActivityListmainBinding

class HomeMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListmainBinding
    private lateinit var userArrayList: ArrayList<Flowers>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListmainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize your data
        val imageId = intArrayOf(
            R.drawable.flowersample, R.drawable.carnation, R.drawable.boutonniere, R.drawable.rose,
            R.drawable.flowersample, R.drawable.carnation, R.drawable.boutonniere, R.drawable.rose,
            R.drawable.flowersample, R.drawable.carnation
        )

        val name = arrayOf(
            "Sunflower", "Carnation", "Boutonniere", "Rose", "Lily", "Tulip", "Daisy", "Orchid", "Daffodil", "Iris"
        )

        val price = arrayOf(
            "200", "300", "400", "500", "250", "350", "450", "550", "275", "375"
        )

        val rating = arrayOf(
            "4.0", "4.5", "5.0", "4.75", "3.5", "4.0", "4.2", "4.8", "3.8", "4.3"
        )

        userArrayList = ArrayList()

        // Populate the ArrayList
        for (i in name.indices) {
            val flower = Flowers(name[i], price[i], rating[i], imageId[i])
            userArrayList.add(flower)
        }

        // Set up the ListView using the binding object
        binding.listView.adapter = AdapterClass(this, userArrayList)
    }
}
