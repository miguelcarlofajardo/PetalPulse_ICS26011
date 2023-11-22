package com.example.finalproject_3ite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import me.relex.circleindicator.CircleIndicator
import me.relex.circleindicator.CircleIndicator3

class MainActivity : AppCompatActivity() {

    private var titleList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imageList = mutableListOf<Int>()

    lateinit var databaseReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        //database - Add
//        databaseReference = FirebaseDatabase.getInstance().getReference("DatabaseName")
//        var databaseClass = DatabaseClass("admin admin", "admin","petalpulse.admin@gmail.com", "admin123")
//        var dataKey = databaseReference.push().getKey();
//        databaseReference.child("uniqueid").setValue(databaseClass).addOnSuccessListener {
//            Toast.makeText(this, "Success - Add", Toast.LENGTH_SHORT).show()
//        }
//
//        //database - Update
//        var empUpdate = mapOf<String, String>("EmpName" to "admin admin1", "EmpUsername" to "admin2", "EmpEmail" to "petalpulse.admin@gmail.com", "EmpPassword" to "admin1234")
//        databaseReference.child("uniqueid").child("SamplOne").updateChildren(empUpdate).addOnSuccessListener {
//            Toast.makeText(this, "Success - Update", Toast.LENGTH_SHORT).show()
//        }
//
//        //database - Remove
//        databaseReference.child("uniqueid").child("SamplOne").removeValue().addOnSuccessListener {
//            Toast.makeText(this, "Success - Delete", Toast.LENGTH_SHORT).show()
//        }
//
//

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