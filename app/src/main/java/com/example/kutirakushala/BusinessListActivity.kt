package com.example.kutirakushala

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore

class BusinessListActivity : AppCompatActivity() {

    private lateinit var recyclerBusinesses: RecyclerView
    private lateinit var btnAll: MaterialButton
    private lateinit var btnFood: MaterialButton
    private lateinit var btnCraft: MaterialButton
    private lateinit var btnTextile: MaterialButton
    private lateinit var etSearch: EditText
    private lateinit var db: FirebaseFirestore

    private var fullList = mutableListOf<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_list)

        db = FirebaseFirestore.getInstance()
        recyclerBusinesses = findViewById(R.id.recyclerBusinesses)
        btnAll = findViewById(R.id.btnAll)
        btnFood = findViewById(R.id.btnFood)
        btnCraft = findViewById(R.id.btnCraft)
        btnTextile = findViewById(R.id.btnTextile)
        etSearch = findViewById(R.id.etSearch)

        recyclerBusinesses.layoutManager = LinearLayoutManager(this)

        loadBusinesses(null)
        updateFilterUI(btnAll)

        btnAll.setOnClickListener { loadBusinesses(null); updateFilterUI(btnAll) }
        btnFood.setOnClickListener { loadBusinesses("Food"); updateFilterUI(btnFood) }
        btnCraft.setOnClickListener { loadBusinesses("Craft"); updateFilterUI(btnCraft) }
        btnTextile.setOnClickListener { loadBusinesses("Textile"); updateFilterUI(btnTextile) }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().lowercase()
                val filteredList = fullList.filter {
                    it["businessName"]?.lowercase()?.contains(searchText) == true ||
                    it["category"]?.lowercase()?.contains(searchText) == true
                }
                recyclerBusinesses.adapter = BusinessAdapter(filteredList) { openBusinessDetail(it) }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateFilterUI(selected: MaterialButton) {
        val buttons = listOf(btnAll, btnFood, btnCraft, btnTextile)
        buttons.forEach { it.alpha = 0.6f }
        selected.alpha = 1.0f
    }

    private fun loadBusinesses(category: String?) {
        val query = if (category != null) {
            db.collection("businesses").whereEqualTo("category", category)
        } else {
            db.collection("businesses")
        }

        query.get().addOnSuccessListener { documents ->
            fullList.clear()
            for (doc in documents.documents) {
                val map = hashMapOf(
                    "id" to doc.id,
                    "businessName" to (doc.getString("businessName") ?: ""),
                    "ownerName" to (doc.getString("ownerName") ?: ""),
                    "category" to (doc.getString("category") ?: ""),
                    "location" to (doc.getString("location") ?: ""),
                    "phone" to (doc.getString("phone") ?: ""),
                    "skillArea" to (doc.getString("skillArea") ?: ""),
                    "weeklyCapacity" to (doc.getString("weeklyCapacity") ?: "0"),
                    "capacityReady" to (doc.getBoolean("capacityReady")?.toString() ?: "false")
                )
                fullList.add(map)
            }
            recyclerBusinesses.adapter = BusinessAdapter(fullList) { openBusinessDetail(it) }
        }.addOnFailureListener {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openBusinessDetail(business: HashMap<String, String>) {
        val intent = Intent(this, BusinessDetailActivity::class.java).apply {
            putExtra("ownerId", business["id"])
            putExtra("businessName", business["businessName"])
            putExtra("location", business["location"])
            putExtra("phone", business["phone"])
            putExtra("category", business["category"])
            putExtra("ownerName", business["ownerName"])
            putExtra("skillArea", business["skillArea"])
            putExtra("weeklyCapacity", business["weeklyCapacity"])
            putExtra("capacityReady", business["capacityReady"])
        }
        startActivity(intent)
    }
}