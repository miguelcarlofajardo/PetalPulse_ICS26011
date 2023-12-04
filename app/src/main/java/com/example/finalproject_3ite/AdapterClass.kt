package com.example.finalproject_3ite

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class AdapterClass(private val context: Activity, private val arrayList: ArrayList<Flowers>) :
    ArrayAdapter<Flowers>(context, R.layout.list_item, arrayList) {

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewById(R.id.profile)
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val rating: TextView = view.findViewById(R.id.productRating)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        // Load image using Picasso
        Picasso.get().load(arrayList[position].imageUrl).into(viewHolder.imageView)

        viewHolder.productName.text = arrayList[position].name
        viewHolder.productPrice.text = arrayList[position].price
        viewHolder.rating.text = arrayList[position].rating

        return view
    }

}
