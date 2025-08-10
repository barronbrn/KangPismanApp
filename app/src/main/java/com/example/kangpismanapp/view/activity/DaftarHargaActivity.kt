package com.example.kangpismanapp.view.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.HargaAdapter
import com.example.kangpismanapp.viewmodel.HargaViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DaftarHargaActivity : AppCompatActivity() {
    private val viewModel: HargaViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var hargaAdapter: HargaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daftar_harga)

        val mainLayout: LinearLayout = findViewById(R.id.main_daftar_harga)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_daftar_harga)
        toolbar.setNavigationOnClickListener { finish() }

        recyclerView = findViewById(R.id.recycler_view_semua_harga)
        hargaAdapter = HargaAdapter()
        recyclerView.adapter = hargaAdapter

        viewModel.semuaDaftarHarga.observe(this) { daftar ->
            hargaAdapter.submitList(daftar)
        }

        // Panggil fungsi untuk mengambil data
        viewModel.fetchSemuaDaftarHarga()
    }
}