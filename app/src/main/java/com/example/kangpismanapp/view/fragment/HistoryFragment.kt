package com.example.kangpismanapp.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.TransaksiAdapter
import com.example.kangpismanapp.util.DialogUtils
import com.example.kangpismanapp.viewmodel.TransaksiViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val viewModel: TransaksiViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var transaksiAdapter: TransaksiAdapter
    private lateinit var emptyStateView: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewHistory)
        progressBar = view.findViewById(R.id.progressBarHistory)
        emptyStateView = view.findViewById(R.id.empty_state_view)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        transaksiAdapter = TransaksiAdapter() // PERUBAHAN 3: Inisialisasi adapter yang benar
        recyclerView.apply {
            adapter = transaksiAdapter // PERUBAHAN 4: Set adapter yang benar
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewModel.transaksiList.observe(viewLifecycleOwner) { list ->
            // Sembunyikan ProgressBar saat data (meskipun kosong) diterima.
            progressBar.visibility = View.GONE

            if (list.isNullOrEmpty()) {
                // Jika daftar kosong, tampilkan pesan
                recyclerView.visibility = View.GONE
                emptyStateView.visibility = View.VISIBLE
            } else {
                // Jika ada data, tampilkan RecyclerView
                recyclerView.visibility = View.VISIBLE
                emptyStateView.visibility = View.GONE
                transaksiAdapter.submitList(list)
            }
        }
    }
}