package com.example.kutirakushala

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddProductActivity : AppCompatActivity() {

    lateinit var etProductName: EditText
    lateinit var etWholesalePrice: EditText
    lateinit var etDailyCapacity: EditText
    lateinit var btnSaveProduct: Button
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        etProductName = findViewById(R.id.etProductName)
        etWholesalePrice = findViewById(R.id.etWholesalePrice)
        etDailyCapacity = findViewById(R.id.etDailyCapacity)
        btnSaveProduct = findViewById(R.id.btnSaveProduct)

        db = FirebaseFirestore.getInstance()

        btnSaveProduct.setOnClickListener {

            val pname = etProductName.text.toString().trim()
            val price = etWholesalePrice.text.toString().trim()
            val capacity = etDailyCapacity.text.toString().trim()

            if (pname.isEmpty() || price.isEmpty() || capacity.isEmpty()) {
                Toast.makeText(this, "Enter all product details", Toast.LENGTH_SHORT).show()
            } else {
                val productMap = hashMapOf(
                    "productName" to pname,
                    "wholesalePrice" to price,
                    "dailyCapacity" to capacity
                )

                db.collection("Products")
                    .add(productMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Product Saved Successfully", Toast.LENGTH_LONG).show()
                        etProductName.text.clear()
                        etWholesalePrice.text.clear()
                        etDailyCapacity.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}