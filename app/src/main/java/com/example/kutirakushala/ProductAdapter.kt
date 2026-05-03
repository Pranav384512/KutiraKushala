package com.example.kutirakushala

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val list: List<HashMap<String, String>>
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvCapacityProduct: TextView = view.findViewById(R.id.tvCapacityProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = list[position]
        holder.tvProductName.text = p["productName"] ?: ""
        holder.tvPrice.text = "💰 Wholesale Price: ₹${p["wholesalePrice"]}"
        holder.tvCapacityProduct.text = "📦 Daily Capacity: ${p["dailyCapacity"]} units"
    }

    override fun getItemCount() = list.size
}