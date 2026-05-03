package com.example.kutirakushala

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ViewProductsActivity : AppCompatActivity() {

    lateinit var recyclerProducts: RecyclerView
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_products)

        db = FirebaseFirestore.getInstance()
        recyclerProducts = findViewById(R.id.recyclerProducts)
        recyclerProducts.layoutManager = LinearLayoutManager(this)

        db.collection("Products")
            .get()
            .addOnSuccessListener { documents ->
                val list = mutableListOf<HashMap<String, String>>()
                for (doc in documents) {
                    val map = hashMapOf(
                        "productName" to (doc.getString("productName") ?: ""),
                        "wholesalePrice" to (doc.getString("wholesalePrice") ?: ""),
                        "dailyCapacity" to (doc.getString("dailyCapacity") ?: "")
                    )
                    list.add(map)
                }
                recyclerProducts.adapter = ProductAdapter(list)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}