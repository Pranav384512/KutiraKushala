package com.example.kutirakushala

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BusinessAdapter(
    private val list: List<HashMap<String, String>>,
    private val onClick: (HashMap<String, String>) -> Unit
) : RecyclerView.Adapter<BusinessAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBusinessName: TextView = view.findViewById(R.id.tvBusinessName)
        val tvOwnerName: TextView = view.findViewById(R.id.tvOwnerName)
        val tvCategoryBusiness: TextView = view.findViewById(R.id.tvCategoryBusiness)
        val tvVillage: TextView = view.findViewById(R.id.tvVillage)
        val tvPhone: TextView = view.findViewById(R.id.tvPhone)
        val tvStatusBadge: TextView = view.findViewById(R.id.tvStatusBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_business, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val b = list[position]

        holder.tvBusinessName.text = b["businessName"] ?: "N/A"
        holder.tvOwnerName.text = "Owner: ${b["ownerName"] ?: "Unknown"}"
        holder.tvCategoryBusiness.text = b["category"] ?: "General"
        holder.tvVillage.text = b["location"] ?: "Unknown Location"
        holder.tvPhone.text = "📞 ${b["phone"] ?: "No Contact"}"

        // Requirement #3: Showing Capacity Status in the list
        val isReady = b["capacityReady"] == "true"
        if (isReady) {
            holder.tvStatusBadge.visibility = View.VISIBLE
            holder.tvStatusBadge.text = "READY: ${b["weeklyCapacity"]} UNITS"
            holder.tvStatusBadge.setBackgroundResource(R.drawable.category_badge_bg) // Reusing the blue/purple bg
        } else {
            holder.tvStatusBadge.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onClick(b)
        }
    }

    override fun getItemCount() = list.size
}