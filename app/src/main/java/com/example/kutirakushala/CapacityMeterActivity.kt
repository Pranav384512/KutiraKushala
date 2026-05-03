package com.example.kutirakushala

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class CapacityMeterActivity : AppCompatActivity() {

    lateinit var etWeeklyCapacity: EditText
    lateinit var btnSaveCapacity: Button
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capacity_meter)

        etWeeklyCapacity = findViewById(R.id.etWeeklyCapacity)
        btnSaveCapacity = findViewById(R.id.btnSaveCapacity)

        db = FirebaseFirestore.getInstance()

        btnSaveCapacity.setOnClickListener {
            val capacity = etWeeklyCapacity.text.toString().trim()

            if (capacity.isEmpty()) {
                Toast.makeText(this, "Enter weekly capacity", Toast.LENGTH_SHORT).show()
            } else {
                val capMap = hashMapOf(
                    "weeklyCapacity" to capacity
                )

                db.collection("CapacityMeter")
                    .add(capMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Capacity Updated Successfully", Toast.LENGTH_LONG).show()
                        etWeeklyCapacity.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}