package com.example.kangpismanapp.view.activity

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kangpismanapp.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UbahPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var editTextPasswordLama: TextInputEditText
    private lateinit var editTextPasswordBaru: TextInputEditText
    private lateinit var editTextKonfirmasiPassword: TextInputEditText
    private lateinit var buttonSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ubah_password)

        val mainLayout: LinearLayout = findViewById(R.id.activity_ubah_pw)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_ubah_password)
        toolbar.setNavigationOnClickListener { finish() }

        editTextPasswordLama = findViewById(R.id.edit_text_password_lama)
        editTextPasswordBaru = findViewById(R.id.edit_text_password_baru)
        editTextKonfirmasiPassword = findViewById(R.id.edit_text_konfirmasi_password)
        buttonSimpan = findViewById(R.id.button_simpan_password)

        buttonSimpan.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        val oldPassword = editTextPasswordLama.text.toString().trim()
        val newPassword = editTextPasswordBaru.text.toString().trim()
        val confirmPassword = editTextKonfirmasiPassword.text.toString().trim()

        // 1. Validasi Input
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            return
        }
        if (newPassword.length < 6) {
            Toast.makeText(this, "Password baru minimal harus 6 karakter", Toast.LENGTH_SHORT).show()
            return
        }
        if (newPassword != confirmPassword) {
            Toast.makeText(this, "Password baru dan konfirmasi tidak cocok", Toast.LENGTH_SHORT).show()
            return
        }

        val user = auth.currentUser
        if (user?.email == null) {
            Toast.makeText(this, "Gagal mendapatkan data pengguna.", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Re-autentikasi (Verifikasi Password Lama)
        val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
        user.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    // 3. Jika verifikasi berhasil, perbarui password
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Toast.makeText(this, "Password berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                                finish() // Kembali ke halaman sebelumnya
                            } else {
                                Toast.makeText(this, "Gagal memperbarui password: ${updateTask.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    // Jika verifikasi gagal (password lama salah)
                    Toast.makeText(this, "Password saat ini salah.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}