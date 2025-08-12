package com.example.kangpismanapp.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kangpismanapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class LoginActivity : AppCompatActivity() {
    // Deklarasi variabel
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewToRegister: TextView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var buttonLoginGoogle: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_login_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi Firebase Auth
        auth = Firebase.auth
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewToRegister = findViewById(R.id.textViewToRegister)

        buttonLogin.setOnClickListener {
            loginUser()
        }

        // Atur aksi ketika teks "Daftar sekarang" ditekan
        textViewToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        setupGoogleLogin()
    }



    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Login Google gagal: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Ambil ID dari file google-services.json
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Hubungkan tombol dan atur listener
        buttonLoginGoogle = findViewById(R.id.button_login_google)
        buttonLoginGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun loginUser() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPword.text.toString().trim()

        // Validasi input
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

        Toast.makeText(this, "Mencoba masuk...", Toast.LENGTH_SHORT).show()

        // Gunakan Firebase Auth untuk memverifikasi pengguna
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Panggil fungsi pengecekan yang sama dengan Login Google
                        checkUserRoleAndRedirect(user)
                    } else {
                        Toast.makeText(baseContext, "Gagal mendapatkan data pengguna.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login ke Firebase berhasil
                    val user = auth.currentUser
                    if (user != null) {
                        // CEK APAKAH PENGGUNA BARU ATAU LAMA
                        checkUserRoleAndRedirect(user)
                    }
                } else {
                    Toast.makeText(this, "Autentikasi Firebase Gagal", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserRoleAndRedirect(user: FirebaseUser) {
        val db = Firebase.firestore
        val userRef = db.collection("users").document(user.uid)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists() && document.contains("role")) {
                    // PENGGUNA LAMA: Ambil perannya dari dokumen
                    val role = document.getString("role") ?: "warga" // Ambil peran, default ke warga
                    Toast.makeText(this, "Login Berhasil.", Toast.LENGTH_SHORT).show()
                    goToMainActivity(role)
                } else {
                    // PENGGUNA BARU: Arahkan ke halaman Pilih Peran
                    Toast.makeText(this, "Selamat datang! Silakan pilih peran Anda.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, PilihPeranActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memeriksa data pengguna.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun goToMainActivity(role: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER_ROLE", role)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}