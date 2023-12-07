package com.example.finalproject_3ite

import ProductClass
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class AdapterClass(private val context: Activity, private val productList: ArrayList<ProductClass>) :
    ArrayAdapter<ProductClass>(context, R.layout.list_item, productList) {

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewById(R.id.profile)
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val size: TextView = view.findViewById(R.id.productRating)
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
        Picasso.get().load(productList[position].imageUrl).into(viewHolder.imageView)

        viewHolder.productName.text = productList[position].name
        viewHolder.productPrice.text = productList[position].price
        viewHolder.size.text = productList[position].size

        // Set click listener for the item
        view.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            val product = productList[position]
            intent.putExtra("productId", product.productId)
            intent.putExtra("productName", product.name)
            intent.putExtra("productPrice", product.price)
            intent.putExtra("productSize", product.size)
            intent.putExtra("imageUrl", product.imageUrl)
            context.startActivity(intent)
        }

        return view
    }
}
