package com.example.kutirakushala

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CapacityMeterActivity : AppCompatActivity() {

    private lateinit var etWeeklyCapacity: EditText
    private lateinit var switchReady: MaterialSwitch
    private lateinit var btnSaveCapacity: MaterialButton
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capacity_meter)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        etWeeklyCapacity = findViewById(R.id.etWeeklyCapacity)
        switchReady = findViewById(R.id.switchReady)
        btnSaveCapacity = findViewById(R.id.btnSaveCapacity)

        // Load current capacity
        loadCurrentCapacity()

        btnSaveCapacity.setOnClickListener {
            saveCapacity()
        }
    }

    private fun loadCurrentCapacity() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("businesses").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    etWeeklyCapacity.setText(doc.getString("weeklyCapacity") ?: "")
                    switchReady.isChecked = doc.getBoolean("capacityReady") ?: true
                }
            }
    }

    private fun saveCapacity() {
        val capacity = etWeeklyCapacity.text.toString().trim()
        val isReady = switchReady.isChecked
        val uid = auth.currentUser?.uid ?: return

        if (capacity.isEmpty()) {
            Toast.makeText(this, "Please enter capacity units", Toast.LENGTH_SHORT).show()
            return
        }

        val capMap = hashMapOf(
            "weeklyCapacity" to capacity,
            "capacityReady" to isReady
        )

        btnSaveCapacity.isEnabled = false
        db.collection("businesses").document(uid)
            .update(capMap as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Live Capacity Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                btnSaveCapacity.isEnabled = true
                Toast.makeText(this, "Update failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}