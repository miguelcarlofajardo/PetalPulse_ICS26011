package com.example.finalproject_3ite

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter (private var title: List<String>, private var details: List<String>,private var images: List<Int>) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val itemTitle : TextView = itemView.findViewById(R.id.txtPetalPulse)
        val itemDetails : TextView = itemView.findViewById(R.id.txtDescription)
        val itemImage : ImageView = itemView.findViewById(R.id.imgImages)
        val btnLogin: Button = itemView.findViewById(R.id.btnLogin)
        val btnRegister: Button = itemView.findViewById(R.id.btnRegister)
    }
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewPagerAdapter.Pager2ViewHolder {
       return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page,parent,false))
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
        holder.itemDetails.text = details[position]
        holder.itemImage.setImageResource(images[position])

        if (position == itemCount - 1) {
            // Hide description and show the buttons for the last page
            holder.itemDetails.visibility = View.GONE
            holder.btnLogin.visibility = View.VISIBLE
            holder.btnRegister.visibility = View.VISIBLE

            holder.btnLogin.setOnClickListener {
                // Handle button click
                Toast.makeText(holder.itemView.context, "Login Button Clicked!", Toast.LENGTH_SHORT).show()

                // Create an Intent to start the LoginActivity (replace LoginActivity::class.java with your actual LoginActivity class)
                val intent = Intent(holder.itemView.context, LoginActivity::class.java)
                holder.itemView.context.startActivity(intent)
            }

            holder.btnRegister.setOnClickListener {
                // Handle button click
                Toast.makeText(holder.itemView.context, "Register Button Clicked!", Toast.LENGTH_SHORT).show()

                // Create an Intent to start the RegisterActivity (replace RegisterActivity::class.java with your actual RegisterActivity class)
                val intent = Intent(holder.itemView.context, RegisterActivity::class.java)
                holder.itemView.context.startActivity(intent)
            }
        } else {
            // Show description and hide the button for other pages
            holder.itemDetails.visibility = View.VISIBLE
            holder.btnLogin.visibility = View.GONE
            holder.btnRegister.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return title.size
    }
}