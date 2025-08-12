package com.example.kangpismanapp.view.activity

import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.TimbangAdapter
import com.example.kangpismanapp.data.model.Transaksi
import com.google.android.material.appbar.MaterialToolbar
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class DetailTransaksiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaksi)

        val mainLayout: LinearLayout = findViewById(R.id.main_detail_transaksi)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_detail)
        toolbar.setNavigationOnClickListener { finish() }

        val textTanggal: TextView = findViewById(R.id.text_detail_tanggal)
        val textTotal: TextView = findViewById(R.id.text_detail_total)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_detail_item)

        // Ambil data transaksi yang dikirim
        val transaksi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("TRANSACTION_DATA", Transaksi::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("TRANSACTION_DATA")
        }

        if (transaksi != null) {
            // Tampilkan data ringkasan
            val dateFormat = SimpleDateFormat("dd MMMM yyyy • HH:mm", Locale("id", "ID"))
            val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            formatRupiah.maximumFractionDigits = 0

            textTanggal.text = transaksi.tanggal?.let { dateFormat.format(it) } ?: "Tanggal tidak tersedia"
            textTotal.text = "Total: ${formatRupiah.format(transaksi.totalRupiah)}"

            // Tampilkan rincian item di RecyclerView
            val detailAdapter = TimbangAdapter() // Kita bisa gunakan TimbangAdapter karena layout-nya mirip
            recyclerView.adapter = detailAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            detailAdapter.submitList(transaksi.items)
        }
    }
}