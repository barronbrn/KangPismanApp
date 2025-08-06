package com.example.kangpismanapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kangpismanapp.R
import com.example.kangpismanapp.view.activity.LoginActivity
import com.example.kangpismanapp.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var textUserEmail: TextView
    private lateinit var textTotalPoin: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var profileContent: ScrollView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Views
        textUserEmail = view.findViewById(R.id.text_user_email)
        textTotalPoin = view.findViewById(R.id.text_total_poin)
        progressBar = view.findViewById(R.id.progress_bar_profile)
        profileContent = view.findViewById(R.id.profile_content)
        val buttonLogout: Button = view.findViewById(R.id.buttonLogout)

        // Setup listener logout
        buttonLogout.setOnClickListener {
            Firebase.auth.signOut()
            Toast.makeText(requireContext(), "Anda telah logout.", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            profileContent.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }

        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                textUserEmail.text = it.email
                val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                textTotalPoin.text = formatRupiah.format(it.totalPoin)
            }
        }
    }
}