package com.example.kutirakushala

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class BusinessProfileActivity : AppCompatActivity() {

    private lateinit var etOwnerName: EditText
    private lateinit var etBusinessName: EditText
    private lateinit var etSkillArea: EditText
    private lateinit var etLocation: EditText
    private lateinit var etPhone: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnSaveProfile: Button
    private lateinit var tvProfileStatus: TextView
    private lateinit var ivProfilePhoto: ImageView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            ivProfilePhoto.setImageURI(selectedImageUri)
            ivProfilePhoto.setPadding(0, 0, 0, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_profile)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        etOwnerName = findViewById(R.id.etOwnerName)
        etBusinessName = findViewById(R.id.etBusinessName)
        etSkillArea = findViewById(R.id.etSkillArea)
        etLocation = findViewById(R.id.etLocation)
        etPhone = findViewById(R.id.etPhone)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)
        tvProfileStatus = findViewById(R.id.tvProfileStatus)
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto)

        findViewById<View>(R.id.cardProfilePhoto).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            imagePickerLauncher.launch(intent)
        }

        val categories = listOf("Food", "Craft", "Textile", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        loadExistingProfile()

        btnSaveProfile.setOnClickListener {
            if (selectedImageUri != null) {
                uploadImageAndSaveProfile()
            } else {
                saveProfile("")
            }
        }
    }

    private fun loadExistingProfile() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("businesses").document(uid).get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                etOwnerName.setText(doc.getString("ownerName") ?: "")
                etBusinessName.setText(doc.getString("businessName") ?: "")
                etSkillArea.setText(doc.getString("skillArea") ?: "")
                etLocation.setText(doc.getString("location") ?: "")
                etPhone.setText(doc.getString("phone") ?: "")
                
                val savedCategory = doc.getString("category") ?: "Food"
                val index = listOf("Food", "Craft", "Textile", "Other").indexOf(savedCategory)
                if (index >= 0) spinnerCategory.setSelection(index)
            }
        }
    }

    private fun uploadImageAndSaveProfile() {
        val uid = auth.currentUser?.uid ?: return
        val fileName = "profile_$uid.jpg"
        val ref = storage.reference.child("profiles/$fileName")

        btnSaveProfile.isEnabled = false
        tvProfileStatus.text = "Uploading image..."

        ref.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    saveProfile(uri.toString())
                }
            }
            .addOnFailureListener {
                btnSaveProfile.isEnabled = true
                Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProfile(imageUrl: String) {
        val uid = auth.currentUser?.uid ?: return
        val profileMap = mutableMapOf(
            "ownerName" to etOwnerName.text.toString(),
            "businessName" to etBusinessName.text.toString(),
            "skillArea" to etSkillArea.text.toString(),
            "location" to etLocation.text.toString(),
            "phone" to etPhone.text.toString(),
            "category" to spinnerCategory.selectedItem.toString(),
            "userId" to uid
        )
        
        if (imageUrl.isNotEmpty()) {
            profileMap["businessImageUrl"] = imageUrl
        }

        tvProfileStatus.text = "Updating profile..."
        db.collection("businesses").document(uid).set(profileMap, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                btnSaveProfile.isEnabled = true
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
