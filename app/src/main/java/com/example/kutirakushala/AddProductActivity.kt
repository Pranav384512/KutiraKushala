package com.example.kutirakushala

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddProductActivity : AppCompatActivity() {

    private lateinit var etProductName: TextInputEditText
    private lateinit var etWholesalePrice: TextInputEditText
    private lateinit var etDailyCapacity: TextInputEditText
    private lateinit var btnSaveProduct: MaterialButton
    private lateinit var spCategory: Spinner
    private lateinit var ivProductPhoto: ImageView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var selectedImageUri: Uri? = null

    // Photo Selection Launcher (Requirement: Product Catalog with photos)
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            ivProductPhoto.setImageURI(selectedImageUri)
            ivProductPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        etProductName = findViewById(R.id.etProductName)
        etWholesalePrice = findViewById(R.id.etWholesalePrice)
        etDailyCapacity = findViewById(R.id.etDailyCapacity)
        btnSaveProduct = findViewById(R.id.btnSaveProduct)
        spCategory = findViewById(R.id.spCategory)
        ivProductPhoto = findViewById(R.id.ivProductPhoto)

        findViewById<androidx.cardview.widget.CardView>(R.id.cardProductPhoto).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            imagePickerLauncher.launch(intent)
        }

        val categories = arrayOf("Food", "Craft", "Textile", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spCategory.adapter = adapter

        btnSaveProduct.setOnClickListener {
            saveProduct()
        }
    }

    private fun saveProduct() {
        val pname = etProductName.text.toString().trim()
        val price = etWholesalePrice.text.toString().trim()
        val capacity = etDailyCapacity.text.toString().trim()
        val category = spCategory.selectedItem.toString()
        val uid = auth.currentUser?.uid ?: return

        if (pname.isEmpty() || price.isEmpty() || capacity.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val productMap = hashMapOf(
            "productName" to pname,
            "wholesalePrice" to price,
            "dailyCapacity" to capacity,
            "category" to category,
            "ownerId" to uid,
            "imageUrl" to (selectedImageUri?.toString() ?: "") // Storing local URI for now
        )

        btnSaveProduct.isEnabled = false
        db.collection("Products")
            .add(productMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Product Listed Successfully!", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                btnSaveProduct.isEnabled = true
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}