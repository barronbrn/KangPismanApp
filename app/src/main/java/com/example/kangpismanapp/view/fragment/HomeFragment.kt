package com.example.kangpismanapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.ArtikelAdapter
import com.example.kangpismanapp.adapter.ArtikelHomeAdapter
import com.example.kangpismanapp.adapter.HargaAdapter
import com.example.kangpismanapp.adapter.TransaksiRingkasAdapter
import com.example.kangpismanapp.view.activity.BuatSetoranActivity
import com.example.kangpismanapp.view.activity.DaftarHargaActivity
import com.example.kangpismanapp.view.activity.DetailArtikelActivity
import com.example.kangpismanapp.view.activity.EdukasiActivity
import com.example.kangpismanapp.viewmodel.EdukasiViewModel
import com.example.kangpismanapp.viewmodel.HargaViewModel
import com.example.kangpismanapp.viewmodel.ProfileViewModel
import com.example.kangpismanapp.viewmodel.TransaksiViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.jvm.java
import java.text.NumberFormat
import java.util.Locale
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var textSaldo: TextView
    private lateinit var textPoin: TextView
    private lateinit var textSampah: TextView
    private lateinit var buttonSetorSampah: LinearLayout
    private lateinit var buttonLokasiBank: LinearLayout
    private lateinit var buttonReward: LinearLayout
    private lateinit var buttonEdukasi: LinearLayout
    private lateinit var linkLihatSemuaTransaksi: TextView
    private lateinit var linkSelengkapnyaHarga: TextView
    private lateinit var buttonLihatSemuaEdukasi: Button

    private lateinit var recyclerViewHarga: RecyclerView
    private lateinit var hargaAdapter: HargaAdapter
    private lateinit var recyclerViewTransaksi: RecyclerView
    private lateinit var transaksiRingkasAdapter: TransaksiRingkasAdapter
    private lateinit var recyclerViewEdukasi: RecyclerView
    private lateinit var artikelHomeAdapter: ArtikelHomeAdapter

    private val profileViewModel: ProfileViewModel by viewModels()
    private val hargaViewModel: HargaViewModel by viewModels()
    private val transaksiViewModel: TransaksiViewModel by viewModels()
    private val edukasiViewModel: EdukasiViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textSaldo = view.findViewById(R.id.text_saldo)
        textPoin = view.findViewById(R.id.text_poin)
        textSampah = view.findViewById(R.id.text_sampah)
        buttonSetorSampah = view.findViewById(R.id.button_setor_sampah)
        buttonLokasiBank = view.findViewById(R.id.button_lokasi_bank)
        buttonReward = view.findViewById(R.id.button_reward)
        buttonEdukasi = view.findViewById(R.id.button_edukasi)
        linkLihatSemuaTransaksi = view.findViewById(R.id.link_lihat_semua_transaksi)
        linkSelengkapnyaHarga = view.findViewById(R.id.link_selengkapnya_harga)
        buttonLihatSemuaEdukasi = view.findViewById(R.id.button_lihat_semua_edukasi)
        recyclerViewHarga = view.findViewById(R.id.recyclerViewHarga)
        recyclerViewTransaksi = view.findViewById(R.id.recycler_view_transaksi_terakhir)
        recyclerViewEdukasi = view.findViewById(R.id.recycler_view_edukasi)

        setupRecyclerViews()
        setupActionButtons()
        observeViewModels()
    }

    private fun setupRecyclerViews() {
        // Setup untuk Harga
        hargaAdapter = HargaAdapter()
        recyclerViewHarga.adapter = hargaAdapter
        recyclerViewHarga.isNestedScrollingEnabled = false // Penting untuk di dalam ScrollView

        // Setup untuk Transaksi
        transaksiRingkasAdapter = TransaksiRingkasAdapter()
        recyclerViewTransaksi.adapter = transaksiRingkasAdapter
        recyclerViewTransaksi.isNestedScrollingEnabled = false
        recyclerViewTransaksi.layoutManager = LinearLayoutManager(requireContext())

        // Setup untuk Edukasi
        artikelHomeAdapter = ArtikelHomeAdapter { artikel ->
            Toast.makeText(requireContext(), "Membuka artikel: ${artikel.title}", Toast.LENGTH_SHORT).show()
        }
        recyclerViewEdukasi.adapter = artikelHomeAdapter

    }

    private fun observeViewModels() {
        // Mengamati data profil untuk kartu ringkasan
        profileViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                formatRupiah.maximumFractionDigits = 0

                textSaldo.text = formatRupiah.format(it.totalSaldo)
                textPoin.text = "${it.totalPoin} points"
                textSampah.text = "%.1f kg".format(it.totalBeratKg)
            }
        }

        // Mengamati daftar harga
        hargaViewModel.daftarHarga.observe(viewLifecycleOwner) { daftar ->
            hargaAdapter.submitList(daftar)
        }

        // Mengamati transaksi terakhir
        transaksiViewModel.latestTransaksiList.observe(viewLifecycleOwner) { list ->
            transaksiRingkasAdapter.submitList(list)
        }

        // Mengamati artikel edukasi
        edukasiViewModel.articles.observe(viewLifecycleOwner) { articles ->
            artikelHomeAdapter.submitList(articles.take(5))
        }
    }

    private fun setupActionButtons() {
        buttonSetorSampah.setOnClickListener {
            startActivity(Intent(activity, BuatSetoranActivity::class.java))
        }
        buttonLokasiBank.setOnClickListener {
            navigateToBottomNavTab(R.id.nav_lokasi)
        }
        buttonReward.setOnClickListener {
            navigateToBottomNavTab(R.id.nav_reward)
        }
        buttonEdukasi.setOnClickListener {
            startActivity(Intent(activity, EdukasiActivity::class.java))
        }
        linkLihatSemuaTransaksi.setOnClickListener {
            navigateToBottomNavTab(R.id.nav_riwayat)
        }
        linkSelengkapnyaHarga.setOnClickListener {
            startActivity(Intent(activity, DaftarHargaActivity::class.java))
        }
        buttonLihatSemuaEdukasi.setOnClickListener {
            startActivity(Intent(activity, EdukasiActivity::class.java))
        }

    }

    private fun navigateToBottomNavTab(itemId: Int) {
        val bottomNavView = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavView?.selectedItemId = itemId
    }
}