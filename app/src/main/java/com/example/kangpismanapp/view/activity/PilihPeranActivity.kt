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

    // Deklarasikan variabel untuk Firestore
    private val db = Firebase.firestore
    private var namaLengkap: String? = null
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_peran)

        // Ambil data yang dikirim dari RegisterActivity
        namaLengkap = intent.getStringExtra("NAMA_LENGKAP")
        username = intent.getStringExtra("USERNAME")

        val radioGroup: RadioGroup = findViewById(R.id.radio_group_role_pilih)
        val saveButton: Button = findViewById(R.id.button_simpan_peran)

        saveButton.setOnClickListener {
            val selectedRoleId = radioGroup.checkedRadioButtonId
            if (selectedRoleId == -1) {
                // Penanganan jika tidak ada yang dipilih
                Toast.makeText(this, "Silakan pilih salah satu peran.", Toast.LENGTH_SHORT).show()
            } else {
                val role = if (selectedRoleId == R.id.radio_button_warga_pilih) "warga" else "petugas"
                saveRoleAndContinue(role)
            }
        }
    }

    private fun saveRoleAndContinue(role: String) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(this, "Sesi tidak valid, silakan login ulang.", Toast.LENGTH_SHORT).show()
            return
        }

        val userData = hashMapOf(
            "uid" to user.uid,
            "email" to user.email,
            "role" to role,
            "namaLengkap" to (namaLengkap ?: ""),
            "username" to (username ?: "")
        )

        db.collection("users").document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "Peran berhasil disimpan!", Toast.LENGTH_SHORT).show()
                goToMainActivity(role)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan peran. Coba lagi.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun goToMainActivity(role: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("USER_ROLE", role)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}