package com.example.kangpismanapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.RewardAdapter
import com.example.kangpismanapp.util.DialogUtils
import com.example.kangpismanapp.viewmodel.ProfileViewModel
import com.example.kangpismanapp.viewmodel.RewardViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardFragment : Fragment(R.layout.fragment_reward) {

    private val rewardViewModel: RewardViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var rewardAdapter: RewardAdapter
    private lateinit var textTotalPoin: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi views
        recyclerView = view.findViewById(R.id.recycler_view_rewards)
        textTotalPoin = view.findViewById(R.id.text_total_poin_reward)

        // Setup RecyclerView
        rewardAdapter = RewardAdapter()
        recyclerView.adapter = rewardAdapter

        // Observe ViewModels
        rewardViewModel.rewardsList.observe(viewLifecycleOwner) { rewards ->
            rewardAdapter.submitList(rewards)
        }

        profileViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                textTotalPoin.text = "${it.totalPoin} pts"
            }
        }
    }
}