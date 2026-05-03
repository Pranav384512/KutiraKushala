package com.example.kutirakushala

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BusinessProfileActivity : AppCompatActivity() {

    lateinit var etOwnerName: EditText
    lateinit var etBusinessName: EditText
    lateinit var etSkillArea: EditText
    lateinit var etLocation: EditText
    lateinit var etPhone: EditText
    lateinit var spinnerCategory: Spinner
    lateinit var btnSaveProfile: Button
    lateinit var tvProfileStatus: TextView
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_profile)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        etOwnerName = findViewById(R.id.etOwnerName)
        etBusinessName = findViewById(R.id.etBusinessName)
        etSkillArea = findViewById(R.id.etSkillArea)
        etLocation = findViewById(R.id.etLocation)
        etPhone = findViewById(R.id.etPhone)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)
        tvProfileStatus = findViewById(R.id.tvProfileStatus)

        // Setup category spinner
        val categories = listOf("Food", "Craft", "Textile", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        // Load existing profile if it exists
        loadExistingProfile()

        btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadExistingProfile() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("businesses").document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    etOwnerName.setText(doc.getString("ownerName") ?: "")
                    etBusinessName.setText(doc.getString("businessName") ?: "")
                    etSkillArea.setText(doc.getString("skillArea") ?: "")
                    etLocation.setText(doc.getString("location") ?: "")
                    etPhone.setText(doc.getString("phone") ?: "")

                    // Set spinner to saved category
                    val savedCategory = doc.getString("category") ?: "Food"
                    val categories = listOf("Food", "Craft", "Textile", "Other")
                    val index = categories.indexOf(savedCategory)
                    if (index >= 0) spinnerCategory.setSelection(index)

                    tvProfileStatus.text = "✅ Profile loaded"
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Could not load profile: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProfile() {
        val ownerName = etOwnerName.text.toString().trim()
        val businessName = etBusinessName.text.toString().trim()
        val skillArea = etSkillArea.text.toString().trim()
        val location = etLocation.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val category = spinnerCategory.selectedItem.toString()

        if (ownerName.isEmpty() || businessName.isEmpty() || skillArea.isEmpty()
            || location.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = auth.currentUser?.uid
        if (uid == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        btnSaveProfile.isEnabled = false
        tvProfileStatus.text = "Saving..."

        val profileMap = hashMapOf(
            "ownerName" to ownerName,
            "businessName" to businessName,
            "skillArea" to skillArea,
            "location" to location,
            "phone" to phone,
            "category" to category,
            "capacityReady" to false,
            "capacityNote" to "",
            "userId" to uid
        )

        // Use UID as document ID so each user has one profile
        db.collection("businesses").document(uid)
            .set(profileMap)
            .addOnSuccessListener {
                btnSaveProfile.isEnabled = true
                tvProfileStatus.text = "✅ Profile saved successfully!"
                Toast.makeText(this, "Profile Saved!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                btnSaveProfile.isEnabled = true
                tvProfileStatus.text = "❌ Failed to save"
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}