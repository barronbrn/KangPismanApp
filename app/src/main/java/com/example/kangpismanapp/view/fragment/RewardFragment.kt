package com.example.kangpismanapp.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.RewardAdapter
import com.example.kangpismanapp.data.model.Reward
import com.example.kangpismanapp.util.DialogUtils
import com.example.kangpismanapp.viewmodel.ProfileViewModel
import com.example.kangpismanapp.viewmodel.RewardViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardFragment : Fragment(R.layout.fragment_reward) {

    private val rewardViewModel: RewardViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var rewardAdapter: RewardAdapter
    private lateinit var textTotalPoin: TextView
    private var currentUserPoints = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_rewards)
        textTotalPoin = view.findViewById(R.id.text_total_poin_reward)

        setupRecyclerView()
        observeViewModels()
    }

    private fun setupRecyclerView() {
        // Inisialisasi adapter dengan aksi klik yang baru
        rewardAdapter = RewardAdapter { reward ->
            showConfirmationDialog(reward)
        }
        recyclerView.adapter = rewardAdapter
    }

    private fun observeViewModels() {
        rewardViewModel.rewardsList.observe(viewLifecycleOwner) { rewards ->
            rewardAdapter.submitList(rewards)
        }

        profileViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                currentUserPoints = it.totalPoin // Simpan total poin pengguna saat ini
                textTotalPoin.text = "$currentUserPoints pts"
            }
        }
    }

    // FUNGSI BARU UNTUK MENAMPILKAN DIALOG
    private fun showConfirmationDialog(reward: Reward) {
        if (currentUserPoints < reward.points) {
            // Jika poin tidak cukup, langsung tampilkan pesan
            Toast.makeText(requireContext(), "Poin Anda tidak cukup untuk menukar reward ini.", Toast.LENGTH_SHORT).show()
            return
        }

        // Jika poin cukup, tampilkan dialog konfirmasi
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Konfirmasi Penukaran")
            .setMessage("Anda akan menukar ${reward.points} poin dengan '${reward.title}'. Apakah Anda yakin?")
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Tukar") { dialog, _ ->
                // Logika penukaran poin yang sebenarnya akan ditambahkan di sini nanti
                // Untuk sekarang, kita tampilkan pesan "fitur dalam pengembangan"
                Toast.makeText(requireContext(), "Fitur penukaran poin masih dalam tahap pengembangan. Terima kasih!", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            .show()
    }
}