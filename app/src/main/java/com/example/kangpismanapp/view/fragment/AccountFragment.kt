package com.example.kangpismanapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.kangpismanapp.R
import com.example.kangpismanapp.data.model.UserProfile
import com.example.kangpismanapp.util.DialogUtils
import com.example.kangpismanapp.view.activity.EditProfileActivity
import com.example.kangpismanapp.view.activity.LoginActivity
import com.example.kangpismanapp.viewmodel.ProfileViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private val viewModel: ProfileViewModel by viewModels()

    // Deklarasi komponen UI
    private lateinit var textProfileName: TextView
    private lateinit var textMemberSince: TextView
    private lateinit var textDetailNama: TextView
    private lateinit var textDetailEmail: TextView
    private lateinit var imageProfile: ImageView
    private lateinit var buttonLogout: Button
    private lateinit var buttonPengaturanAkun: TextView
    private lateinit var buttonEditProfileImage: ImageView
    private var currentUserProfile: UserProfile? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi semua komponen UI dari layout
        textProfileName = view.findViewById(R.id.text_profile_name)
        textMemberSince = view.findViewById(R.id.text_member_since)
        textDetailNama = view.findViewById(R.id.text_detail_nama)
        textDetailEmail = view.findViewById(R.id.text_detail_email)
        imageProfile = view.findViewById(R.id.image_profile)
        buttonLogout = view.findViewById(R.id.buttonLogout)
        buttonPengaturanAkun = view.findViewById(R.id.button_pengaturan_akun)
        buttonEditProfileImage = view.findViewById(R.id.button_edit_profile_image)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        buttonLogout.setOnClickListener {
            Firebase.auth.signOut()
            Toast.makeText(requireContext(), "Anda telah logout.", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        buttonPengaturanAkun.setOnClickListener {
            currentUserProfile?.let {
                val intent = Intent(activity, EditProfileActivity::class.java)
                intent.putExtra("USER_PROFILE_DATA", it)
                startActivity(intent)
            }
        }

        buttonEditProfileImage.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur ubah foto profil akan segera hadir!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            currentUserProfile = profile // Simpan data profil saat tiba
            profile?.let {
                textProfileName.text = it.username
                textDetailNama.text = it.username
                textDetailEmail.text = it.email

                // Untuk sementara, teks ini kita buat statis
                textMemberSince.text = "Member Sejak 2025"

                Glide.with(this)
                    .load(it.profileImageUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .circleCrop()
                    .into(imageProfile)
            }
        }
    }
}