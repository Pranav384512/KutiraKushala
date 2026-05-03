package com.example.kutirakushala

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BusinessDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_detail)

        val name = intent.getStringExtra("businessName") ?: ""
        val skill = intent.getStringExtra("skillArea") ?: ""
        val location = intent.getStringExtra("location") ?: ""
        val phone = intent.getStringExtra("phone") ?: ""
        val capacityReady = intent.getStringExtra("capacityReady") ?: "false"
        val capacityNote = intent.getStringExtra("capacityNote") ?: ""

        findViewById<TextView>(R.id.tvDetailName).text = name
        findViewById<TextView>(R.id.tvDetailSkill).text = "🧵 $skill"
        findViewById<TextView>(R.id.tvDetailLocation).text = "📍 $location"
        findViewById<TextView>(R.id.tvDetailCapacity).text =
            if (capacityReady == "true")
                "✅ $capacityNote"
            else
                "⚠️ Not taking bulk orders now"

        findViewById<Button>(R.id.btnCallNow).setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(intent)
        }
    }
}