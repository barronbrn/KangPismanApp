package com.example.kangpismanapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kangpismanapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    // Deklarasikan variabel untuk Firebase Auth dan komponen UI
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var textViewToLogin: TextView
    private lateinit var editTextUsername: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi Firebase Auth
        auth = Firebase.auth

        // Hubungkan variabel dengan komponen UI dari layout XML
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        textViewToLogin = findViewById(R.id.textViewToLogin)
        editTextUsername = findViewById(R.id.editTextUsername)

        // Atur aksi ketika tombol "Daftar" ditekan
        buttonRegister.setOnClickListener {
            registerUser()
        }

        // Atur aksi ketika teks "Masuk di sini" ditekan
        textViewToLogin.setOnClickListener {
            // Pindah ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPword.text.toString().trim()
        val username = editTextUsername.text.toString().trim()

        if (username.isEmpty()) {
            editTextUsername.error = "Username tidak boleh kosong"
            editTextUsername.requestFocus()
            return
        }

        if (email.isEmpty()) {
            editTextEmail.error = "Email tidak boleh kosong"
            editTextEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            editTextPword.error = "Password tidak boleh kosong"
            editTextPword.requestFocus()
            return
        }

        Toast.makeText(this, "Membuat akun...", Toast.LENGTH_SHORT).show()

        // Gunakan Firebase Auth untuk membuat pengguna baru
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Jika pendaftaran di Auth berhasil, dapatkan info user
                    val user = auth.currentUser
                    if (user != null) {
                        // Panggil untuk menyimpan data username ke Firestore
                        saveUserInfoToFirestore(user.uid, user.email ?: "", username)
                    } else {
                        // Penanganan jika user tiba-tiba null
                        Toast.makeText(baseContext, "Gagal mendapatkan data pengguna.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun saveUserInfoToFirestore(uid: String, email: String, username: String) {
        val user = mapOf(
            "uid" to uid,
            "email" to email,
            "username" to username
        )

        Firebase.firestore.collection("users").document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "User info saved successfully")
                // Pindah ke Login setelah info dasar disimpan
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(baseContext, "Gagal menyimpan data pengguna: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}