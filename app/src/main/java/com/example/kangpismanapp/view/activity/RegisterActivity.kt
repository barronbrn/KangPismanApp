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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    // Deklarasikan variabel untuk Firebase Auth dan komponen UI
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var textViewToLogin: TextView
    private lateinit var editTextUsername: TextInputEditText
    private lateinit var editTextNamaLengkap: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        textViewToLogin = findViewById(R.id.textViewToLogin)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextNamaLengkap = findViewById(R.id.editTextNamaLengkap)

        buttonRegister.setOnClickListener {
            registerUser()
        }

        textViewToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerUser() {
        val namaLengkap = editTextNamaLengkap.text.toString().trim()
        val username = editTextUsername.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (namaLengkap.isEmpty()) {
            editTextNamaLengkap.error = "Nama lengkap tidak boleh kosong"
            return
        }

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
            editTextPassword.error = "Password tidak boleh kosong"
            editTextPassword.requestFocus()
            return
        }

        Toast.makeText(this, "Membuat akun...", Toast.LENGTH_SHORT).show()

        // Gunakan Firebase Auth untuk membuat pengguna baru
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Jika pendaftaran berhasil, JANGAN simpan data di sini.
                    // Pindah ke halaman PilihPeran sambil membawa data.
                    Toast.makeText(this, "Akun berhasil dibuat. Silakan pilih peran Anda.", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, PilihPeranActivity::class.java).apply {
                        putExtra("NAMA_LENGKAP", namaLengkap)
                        putExtra("USERNAME", username)
                    }
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(baseContext, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

}