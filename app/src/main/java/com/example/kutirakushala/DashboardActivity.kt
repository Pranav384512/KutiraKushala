package com.example.kutirakushala

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()

        // CardViews for Navigation
        val cardAddProduct = findViewById<CardView>(R.id.cardAddProduct)
        val cardCapacity = findViewById<CardView>(R.id.cardCapacity)
        val cardViewProducts = findViewById<CardView>(R.id.cardViewProducts)
        val cardBrowseBusinesses = findViewById<CardView>(R.id.cardBrowseBusinesses)
        val cardMyProfile = findViewById<CardView>(R.id.cardMyProfile)
        val ivLogout = findViewById<ImageView>(R.id.ivLogout)

        cardAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        cardCapacity.setOnClickListener {
            startActivity(Intent(this, CapacityMeterActivity::class.java))
        }

        cardViewProducts.setOnClickListener {
            startActivity(Intent(this, ViewProductsActivity::class.java))
        }

        cardBrowseBusinesses.setOnClickListener {
            startActivity(Intent(this, BusinessListActivity::class.java))
        }

        cardMyProfile.setOnClickListener {
            startActivity(Intent(this, BusinessProfileActivity::class.java))
        }

        ivLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}