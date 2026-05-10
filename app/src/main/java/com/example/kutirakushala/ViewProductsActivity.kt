package com.example.kutirakushala

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ViewProductsActivity : AppCompatActivity() {

    private lateinit var recyclerProducts: RecyclerView
    private lateinit var fabAddProduct: FloatingActionButton
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_products)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        
        recyclerProducts = findViewById(R.id.recyclerProducts)
        fabAddProduct = findViewById(R.id.fabAddProduct)
        
        recyclerProducts.layoutManager = LinearLayoutManager(this)

        val ownerId = intent.getStringExtra("ownerId")
        val currentUserId = auth.currentUser?.uid

        // If no ownerId is passed, assume we are viewing OUR OWN products from Dashboard
        val targetId = ownerId ?: currentUserId

        // Hide FAB if viewing someone else's catalog
        if (ownerId != null && ownerId != currentUserId) {
            fabAddProduct.visibility = android.view.View.GONE
        }

        fabAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        loadProducts(targetId)
    }

    private fun loadProducts(userId: String?) {
        if (userId == null) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        // Filtering products by ownerId as per Micro-Factory Showcase requirements
        db.collection("Products")
            .whereEqualTo("ownerId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val list = mutableListOf<HashMap<String, String>>()
                for (doc in documents) {
                    val map = hashMapOf(
                        "productName" to (doc.getString("productName") ?: ""),
                        "wholesalePrice" to (doc.getString("wholesalePrice") ?: ""),
                        "dailyCapacity" to (doc.getString("dailyCapacity") ?: ""),
                        "category" to (doc.getString("category") ?: "General")
                    )
                    list.add(map)
                }
                recyclerProducts.adapter = ProductAdapter(list)
                
                if (list.isEmpty()) {
                    Toast.makeText(this, "No products found in this catalog", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}