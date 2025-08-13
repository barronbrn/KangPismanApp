package com.example.kangpismanapp.view.activity

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
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
    private lateinit var editTextNamaLengkap: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        val mainLayout: LinearLayout = findViewById(R.id.actv_edit)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_edit_profile)
        toolbar.setNavigationOnClickListener { finish() }

        editTextNamaLengkap = findViewById(R.id.edit_text_nama_lengkap)
        editTextUsername = findViewById(R.id.edit_text_username)
        editTextTelepon = findViewById(R.id.edit_text_telepon)
        editTextAlamat = findViewById(R.id.edit_text_alamat)
        val buttonSimpan: Button = findViewById(R.id.button_simpan_profil)

        val userProfile = intent.getParcelableExtra<UserProfile>("USER_PROFILE_DATA")
        userProfile?.let {
            editTextNamaLengkap.setText(it.namaLengkap) // <-- Isi data
            editTextUsername.setText(it.username)
            editTextTelepon.setText(it.noTelepon)
            editTextAlamat.setText(it.alamat)
        }

        buttonSimpan.setOnClickListener {
            val newNamaLengkap = editTextNamaLengkap.text.toString().trim()
            val newUsername = editTextUsername.text.toString().trim()
            val newTelepon = editTextTelepon.text.toString().trim()
            val newAlamat = editTextAlamat.text.toString().trim()

            viewModel.updateUserProfile(newNamaLengkap, newUsername, newTelepon, newAlamat)

            Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}