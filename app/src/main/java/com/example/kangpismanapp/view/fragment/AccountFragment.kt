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
import com.example.kangpismanapp.view.activity.BantuanActivity
import com.example.kangpismanapp.view.activity.EditProfileActivity
import com.example.kangpismanapp.view.activity.LoginActivity
import com.example.kangpismanapp.view.activity.UbahPasswordActivity
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
    private var currentUserProfile: UserProfile? = null

    // Deklarasi komponen UI
    private lateinit var textProfileName: TextView
    private lateinit var textMemberSince: TextView
    private lateinit var imageProfile: ImageView
    private lateinit var textDetailNama: TextView
    private lateinit var textDetailEmail: TextView
    private lateinit var textDetailTelepon: TextView
    private lateinit var textDetailAlamat: TextView
    private lateinit var buttonEditProfileImage: ImageView
    private lateinit var buttonEditProfil: TextView
    private lateinit var buttonUbahPassword: TextView
    private lateinit var buttonBantuan: TextView
    private lateinit var buttonLogout: Button
    private lateinit var textAppVersion: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi semua komponen UI dari layout
        textProfileName = view.findViewById(R.id.text_profile_name)
        textMemberSince = view.findViewById(R.id.text_member_since)
        imageProfile = view.findViewById(R.id.image_profile)
        textDetailNama = view.findViewById(R.id.text_detail_nama)
        textDetailEmail = view.findViewById(R.id.text_detail_email)
        textDetailTelepon = view.findViewById(R.id.text_detail_telepon)
        textDetailAlamat = view.findViewById(R.id.text_detail_alamat)
        buttonEditProfileImage = view.findViewById(R.id.button_edit_profile_image)
        buttonEditProfil = view.findViewById(R.id.button_edit_profil)
        buttonUbahPassword = view.findViewById(R.id.button_ubah_password)
        buttonBantuan = view.findViewById(R.id.button_bantuan)
        buttonLogout = view.findViewById(R.id.buttonLogout)
        textAppVersion = view.findViewById(R.id.text_app_version)

        setupListeners()
        observeViewModel()
        setAppVersion()
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

        buttonEditProfil.setOnClickListener {
            currentUserProfile?.let {
                val intent = Intent(activity, EditProfileActivity::class.java)
                intent.putExtra("USER_PROFILE_DATA", it)
                startActivity(intent)
            }
        }

        buttonUbahPassword.setOnClickListener {
            startActivity(Intent(activity, UbahPasswordActivity::class.java))
        }

        buttonEditProfileImage.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur ubah foto profil akan segera hadir!", Toast.LENGTH_SHORT).show()
        }

        buttonBantuan.setOnClickListener {
            startActivity(Intent(activity, BantuanActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            currentUserProfile = profile
            profile?.let {
                // Penempatan data yang benar
                textProfileName.text = it.namaLengkap
                textDetailNama.text = it.username
                textDetailEmail.text = it.email
                textDetailTelepon.text = it.noTelepon
                textDetailAlamat.text = it.alamat

                textMemberSince.text = "Member Sejak 2025" // Contoh

                Glide.with(this)
                    .load(it.profileImageUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .circleCrop()
                    .into(imageProfile)
            }
        }
    }

    private fun setAppVersion() {
        try {
            val packageInfo = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            val versionName = packageInfo.versionName
            textAppVersion.text = "Versi $versionName"
        } catch (e: Exception) {
            // Tangani jika terjadi error (jarang terjadi)
            textAppVersion.visibility = View.GONE
            e.printStackTrace()
        }
    }
}