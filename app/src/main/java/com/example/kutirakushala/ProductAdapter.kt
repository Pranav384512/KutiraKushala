package com.example.kutirakushala

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(
    private val list: List<HashMap<String, String>>
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvCapacityProduct: TextView = view.findViewById(R.id.tvCapacityProduct)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val ivProductImage: ImageView = view.findViewById(R.id.ivProductImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = list[position]

        holder.tvProductName.text = p["productName"] ?: "Unknown Product"
        holder.tvPrice.text = "₹${p["wholesalePrice"] ?: "0"}"
        holder.tvCapacityProduct.text = "${p["dailyCapacity"] ?: "0"} units"
        holder.tvCategory.text = p["category"] ?: "General"

        val imageUrl = p["imageUrl"]
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivProductImage)
        }
    }

    override fun getItemCount() = list.size
}
