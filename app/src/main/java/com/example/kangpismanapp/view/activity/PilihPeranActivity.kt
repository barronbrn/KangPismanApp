package com.example.kangpismanapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kangpismanapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class PilihPeranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_peran)

        val radioGroup: RadioGroup = findViewById(R.id.radio_group_role_pilih)
        val saveButton: Button = findViewById(R.id.button_simpan_peran)

        saveButton.setOnClickListener {
            val selectedRoleId = radioGroup.checkedRadioButtonId
            val role = if (selectedRoleId == R.id.radio_button_warga_pilih) "warga" else "petugas"

            saveRoleAndContinue(role)
        }
    }

    private fun saveRoleAndContinue(role: String) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            // Jika terjadi error, kembalikan ke login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val userData = mapOf(
            "uid" to user.uid,
            "email" to user.email,
            "role" to role
        )

        Firebase.firestore.collection("users").document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "Peran berhasil disimpan!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan peran. Coba lagi.", Toast.LENGTH_SHORT).show()
            }
    }
}