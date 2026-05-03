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
        val tvSkill: TextView = view.findViewById(R.id.tvSkill)
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
        val tvCapacity: TextView = view.findViewById(R.id.tvCapacity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_business, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val b = list[position]
        holder.tvBusinessName.text = b["businessName"] ?: ""
        holder.tvSkill.text = b["skillArea"] ?: ""
        holder.tvLocation.text = "📍 ${b["location"] ?: ""}"
        holder.tvCapacity.text =
            if (b["capacityReady"] == "true")
                "✅ ${b["capacityNote"]}"
            else ""
        holder.itemView.setOnClickListener { onClick(b) }
    }

    override fun getItemCount() = list.size
}