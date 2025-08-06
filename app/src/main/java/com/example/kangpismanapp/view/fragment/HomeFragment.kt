package com.example.kangpismanapp.view.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.HargaAdapter
import com.example.kangpismanapp.adapter.TransaksiRingkasAdapter
import com.example.kangpismanapp.data.model.Jadwal
import com.example.kangpismanapp.view.activity.TimbangActivity
import com.example.kangpismanapp.viewmodel.HargaViewModel
import com.example.kangpismanapp.viewmodel.TransaksiViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val hargaViewModel: HargaViewModel by viewModels()
    private val transaksiViewModel: TransaksiViewModel by viewModels()
    private lateinit var buttonSetorSampah: LinearLayout
    private lateinit var buttonLokasiBank: LinearLayout
    private lateinit var buttonReward: LinearLayout
    private lateinit var buttonEdukasi: LinearLayout

    // variabel UI untuk harga
    private lateinit var recyclerViewHarga: RecyclerView
    private lateinit var hargaAdapter: HargaAdapter

    // Variabel UI BARU untuk transaksi terakhir
    private lateinit var recyclerViewTransaksi: RecyclerView
    private lateinit var transaksiRingkasAdapter: TransaksiRingkasAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi semua Views
        recyclerViewHarga = view.findViewById(R.id.recyclerViewHarga)
        recyclerViewHarga = view.findViewById(R.id.recyclerViewHarga)
        recyclerViewTransaksi = view.findViewById(R.id.recycler_view_transaksi_terakhir)

        buttonSetorSampah = view.findViewById(R.id.button_setor_sampah)
        buttonLokasiBank = view.findViewById(R.id.button_lokasi_bank)
        buttonReward = view.findViewById(R.id.button_reward)
        buttonEdukasi = view.findViewById(R.id.button_edukasi)

        // progressBar = view.findViewById(R.id.progressBar)
        recyclerViewTransaksi = view.findViewById(R.id.recycler_view_transaksi_terakhir)

        setupRecyclerViews()
        observeViewModels()
    }

    private fun setupRecyclerViews() {
        // Setup untuk harga
        hargaAdapter = HargaAdapter()
        recyclerViewHarga.adapter = hargaAdapter
        recyclerViewHarga.isNestedScrollingEnabled = false

        transaksiRingkasAdapter = TransaksiRingkasAdapter()
        recyclerViewTransaksi.adapter = transaksiRingkasAdapter
        recyclerViewTransaksi.isNestedScrollingEnabled = false
        recyclerViewTransaksi.layoutManager = LinearLayoutManager(requireContext())

        // Setup BARU untuk transaksi
        transaksiRingkasAdapter = TransaksiRingkasAdapter()
        recyclerViewTransaksi.adapter = transaksiRingkasAdapter
        recyclerViewTransaksi.isNestedScrollingEnabled = false
        recyclerViewTransaksi.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModels() {
        // Observe harga (tidak berubah)
        hargaViewModel.daftarHarga.observe(viewLifecycleOwner) { daftar ->
            hargaAdapter.submitList(daftar)
        }

        // ... observe isLoading untuk harga ...

        // Observe BARU untuk transaksi terakhir
        transaksiViewModel.latestTransaksiList.observe(viewLifecycleOwner) { list ->
            transaksiRingkasAdapter.submitList(list)
        }
    }

    private fun setupActionButtons() {
        buttonSetorSampah.setOnClickListener {
            // Buka halaman Timbang Sampah
            startActivity(Intent(activity, TimbangActivity::class.java))
        }

        buttonLokasiBank.setOnClickListener {
            // Pindahkan navigasi ke tab Lokasi
            navigateToBottomNavTab(R.id.nav_lokasi)
        }

        buttonReward.setOnClickListener {
            // Pindahkan navigasi ke tab Reward
            navigateToBottomNavTab(R.id.nav_reward)
        }

        buttonEdukasi.setOnClickListener {
            // Untuk sementara, tampilkan pesan
            Toast.makeText(requireContext(), "Fitur Edukasi akan segera hadir!", Toast.LENGTH_SHORT).show()
        }
    }

    // FUNGSI BANTUAN UNTUK BERPINDAH TAB
    private fun navigateToBottomNavTab(itemId: Int) {
        // Dapatkan BottomNavigationView dari MainActivity
        val bottomNavView = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        // Pindahkan item yang terpilih
        bottomNavView?.selectedItemId = itemId
    }
}