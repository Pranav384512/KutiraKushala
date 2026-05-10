package com.example.kutirakushala

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class BusinessDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_detail)

        // Get data from intent passed from BusinessListActivity
        val ownerId = intent.getStringExtra("ownerId")
        val name = intent.getStringExtra("businessName") ?: "Business Name"
        val location = intent.getStringExtra("location") ?: "Unknown Location"
        val phone = intent.getStringExtra("phone") ?: ""
        val category = intent.getStringExtra("category") ?: "General"
        val owner = intent.getStringExtra("ownerName") ?: "the entrepreneur"
        val skill = intent.getStringExtra("skillArea") ?: "Handmade Goods"
        val capacity = intent.getStringExtra("weeklyCapacity") ?: "0"
        val isReady = intent.getStringExtra("capacityReady") == "true"

        // Bind Views
        findViewById<TextView>(R.id.tvDetailBusinessName).text = name
        findViewById<TextView>(R.id.tvDetailLocation).text = "📍 $location"
        findViewById<TextView>(R.id.tvDetailCategory).text = category
        findViewById<TextView>(R.id.tvDetailSkill).text = "Skill: $skill"
        findViewById<TextView>(R.id.tvDetailOwner).text = "Managed by $owner. Specializing in high-quality $category products directly from our home-based micro-factory."

        // Capacity Meter Status - Requirement #3 & #6
        val tvCapacity = findViewById<TextView>(R.id.tvDetailCapacityStatus)
        if (isReady) {
            tvCapacity.text = "✅ Ready for $capacity units this week"
            tvCapacity.setTextColor(getColor(R.color.accent_green))
        } else {
            tvCapacity.text = "⚠️ Currently at full capacity"
            tvCapacity.setTextColor(getColor(R.color.accent_orange))
        }

        // Direct Connect - Call Button (Requirement #3)
        findViewById<MaterialButton>(R.id.btnCallEntrepreneur).setOnClickListener {
            if (phone.isNotEmpty()) {
                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                startActivity(dialIntent)
            }
        }

        // Product Catalog Showcase (Requirement #3)
        findViewById<MaterialButton>(R.id.btnViewCatalog).setOnClickListener {
            val intent = Intent(this, ViewProductsActivity::class.java).apply {
                putExtra("ownerId", ownerId)
            }
            startActivity(intent)
        }
    }
}