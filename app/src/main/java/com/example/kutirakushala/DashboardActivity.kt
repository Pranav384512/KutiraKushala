package com.example.kutirakushala

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    lateinit var btnAddProduct: Button
    lateinit var btnCapacity: Button
    lateinit var btnViewProducts: Button
    lateinit var btnViewBusinesses: Button
    lateinit var btnLogout: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()

        btnAddProduct = findViewById(R.id.btnAddProduct)
        btnCapacity = findViewById(R.id.btnCapacity)
        btnViewProducts = findViewById(R.id.btnViewProducts)
        btnViewBusinesses = findViewById(R.id.btnViewBusinesses)
        btnLogout = findViewById(R.id.btnLogout)

        btnAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        btnCapacity.setOnClickListener {
            startActivity(Intent(this, CapacityMeterActivity::class.java))
        }

        btnViewProducts.setOnClickListener {
            startActivity(Intent(this, ViewProductsActivity::class.java))
        }

        btnViewBusinesses.setOnClickListener {
            startActivity(Intent(this, BusinessListActivity::class.java))
        }
        val btnMyProfile = findViewById<Button>(R.id.btnMyProfile)
        btnMyProfile.setOnClickListener {
            startActivity(Intent(this, BusinessProfileActivity::class.java))
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}