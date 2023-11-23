package com.example.finalproject_3ite

import android.R.attr.password
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import me.relex.circleindicator.CircleIndicator3


class MainActivity : AppCompatActivity() {

    private var titleList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imageList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val lottieAnimationView: LottieAnimationView = findViewById(R.id.lottieAnimationView)
//        lottieAnimationView.playAnimation()

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
        addToList("PetalPulse", "Explore the beauty of nature with our PetalPulse app. Immerse yourself in a diverse collection of flowers, from vibrant roses to exotic orchids.", R.drawable.womanflowers)
        addToList("PetalPulse", "Embark on a floral journey with our PetalPulse app. Discover a spectrum of blooms, from lively roses to captivating orchids.", R.drawable.woman)
        addToList("PetalPulse", "Indulge in the world of flowers with PetalPulse. Experience a rich assortment of blooms, from dynamic roses to alluring orchids.", R.drawable.women2)
        addToList("PetalPulse", "Immerse yourself in nature's wonders with PetalPulse. Explore an array of blooms, from playful roses to enchanting orchids.", R.drawable.child)
        addToList("PetalPulse", "Dive into the realm of flowers with PetalPulse. Witness a variety of blooms, from majestic roses to graceful orchids.", R.drawable.man)


    }

}