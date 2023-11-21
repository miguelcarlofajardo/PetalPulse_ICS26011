package com.example.finalproject_3ite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator
import me.relex.circleindicator.CircleIndicator3

class MainActivity : AppCompatActivity() {

    private var titleList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imageList = mutableListOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        postToList()

        val viewPager2: ViewPager2 = findViewById(R.id.viewpager2)

        viewPager2.adapter = ViewPagerAdapter(titleList,descList,imageList)
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator: CircleIndicator3 = findViewById(R.id.indicator)
        indicator.setViewPager(viewPager2)

    }
    private fun addToList (title: String, description : String, image: Int){
        titleList.add(title)
        descList.add(description)
        imageList.add(image)
    }

    private fun postToList(){
        for (i in 1..5){
            addToList("PetalPulse","Discover the world of flowers and plants with our PetalPulse app. Explore a vast collection of blooms, from vibrant roses to exotic orchids.\"\n"
                    ,R.drawable.womanflowers)
        }
    }
}