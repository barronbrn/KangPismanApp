package com.example.kangpismanapp.view.activity

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kangpismanapp.R
import com.example.kangpismanapp.data.model.UserProfile
import com.example.kangpismanapp.viewmodel.ProfileViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var editTextUsername: TextInputEditText
    private lateinit var editTextTelepon: TextInputEditText
    private lateinit var editTextAlamat: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_edit_profile)
        toolbar.setNavigationOnClickListener { finish() }

        editTextUsername = findViewById(R.id.edit_text_username)
        editTextTelepon = findViewById(R.id.edit_text_telepon)
        editTextAlamat = findViewById(R.id.edit_text_alamat)
        val buttonSimpan: Button = findViewById(R.id.button_simpan_profil)

        // Ambil data profil yang dikirim dari AccountFragment dan isi formulir
        val userProfile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("USER_PROFILE_DATA", UserProfile::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("USER_PROFILE_DATA")
        }

        userProfile?.let {
            editTextUsername.setText(it.username)
            editTextTelepon.setText(it.noTelepon)
            editTextAlamat.setText(it.alamat)
        }

        buttonSimpan.setOnClickListener {
            // Ambil data baru dari formulir
            val newUsername = editTextUsername.text.toString().trim()
            val newTelepon = editTextTelepon.text.toString().trim()
            val newAlamat = editTextAlamat.text.toString().trim()

            // Panggil fungsi di ViewModel untuk menyimpan perubahan
            viewModel.updateUserProfile(newUsername, newTelepon, newAlamat)

            Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            finish() // Kembali ke halaman Akun
        }
    }
}