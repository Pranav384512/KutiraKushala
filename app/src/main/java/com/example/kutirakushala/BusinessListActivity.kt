package com.example.kutirakushala

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class BusinessListActivity : AppCompatActivity() {

    lateinit var recyclerBusinesses: RecyclerView
    lateinit var btnAll: Button
    lateinit var btnFood: Button
    lateinit var btnCraft: Button
    lateinit var btnTextile: Button
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_list)

        db = FirebaseFirestore.getInstance()
        recyclerBusinesses = findViewById(R.id.recyclerBusinesses)
        btnAll = findViewById(R.id.btnAll)
        btnFood = findViewById(R.id.btnFood)
        btnCraft = findViewById(R.id.btnCraft)
        btnTextile = findViewById(R.id.btnTextile)

        recyclerBusinesses.layoutManager = LinearLayoutManager(this)

        loadBusinesses(null)

        btnAll.setOnClickListener { loadBusinesses(null) }
        btnFood.setOnClickListener { loadBusinesses("Food") }
        btnCraft.setOnClickListener { loadBusinesses("Craft") }
        btnTextile.setOnClickListener { loadBusinesses("Textile") }
    }

    private fun loadBusinesses(category: String?) {
        val query = if (category != null) {
            db.collection("businesses")
                .whereEqualTo("category", category)
        } else {
            db.collection("businesses")
        }

        query.get()
            .addOnSuccessListener { documents ->
                val list = mutableListOf<HashMap<String, String>>()
                for (doc in documents) {
                    val map = hashMapOf(
                        "id" to doc.id,
                        "businessName" to (doc.getString("businessName") ?: ""),
                        "skillArea" to (doc.getString("skillArea") ?: ""),
                        "location" to (doc.getString("location") ?: ""),
                        "phone" to (doc.getString("phone") ?: ""),
                        "capacityReady" to (doc.getBoolean("capacityReady")?.toString() ?: "false"),
                        "capacityNote" to (doc.getString("capacityNote") ?: "")
                    )
                    list.add(map)
                }
                recyclerBusinesses.adapter = BusinessAdapter(list) { business ->
                    val intent = Intent(this, BusinessDetailActivity::class.java)
                    intent.putExtra("businessName", business["businessName"])
                    intent.putExtra("skillArea", business["skillArea"])
                    intent.putExtra("location", business["location"])
                    intent.putExtra("phone", business["phone"])
                    intent.putExtra("capacityReady", business["capacityReady"])
                    intent.putExtra("capacityNote", business["capacityNote"])
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}