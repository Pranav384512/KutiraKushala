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
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddProductActivity : AppCompatActivity() {

    private lateinit var etProductName: TextInputEditText
    private lateinit var etWholesalePrice: TextInputEditText
    private lateinit var etDailyCapacity: TextInputEditText
    private lateinit var btnSaveProduct: MaterialButton
    private lateinit var spCategory: Spinner
    private lateinit var ivProductPhoto: ImageView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private var selectedImageUri: Uri? = null

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
        storage = FirebaseStorage.getInstance()

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
            if (selectedImageUri != null) {
                uploadImageAndSaveProduct()
            } else {
                saveProduct("")
            }
        }
    }

    private fun uploadImageAndSaveProduct() {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = storage.reference.child("products/$fileName")

        btnSaveProduct.isEnabled = false
        Toast.makeText(this, "Uploading image...", Toast.LENGTH_SHORT).show()

        ref.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    saveProduct(uri.toString())
                }
            }
            .addOnFailureListener {
                btnSaveProduct.isEnabled = true
                Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProduct(imageUrl: String) {
        val pname = etProductName.text.toString().trim()
        val price = etWholesalePrice.text.toString().trim()
        val capacity = etDailyCapacity.text.toString().trim()
        val category = spCategory.selectedItem.toString()
        val uid = auth.currentUser?.uid ?: return

        if (pname.isEmpty() || price.isEmpty() || capacity.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            btnSaveProduct.isEnabled = true
            return
        }

        val productMap = hashMapOf(
            "productName" to pname,
            "wholesalePrice" to price,
            "dailyCapacity" to capacity,
            "category" to category,
            "ownerId" to uid,
            "imageUrl" to imageUrl
        )

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
