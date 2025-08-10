package com.example.kangpismanapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.data.model.Transaksi
import com.example.kangpismanapp.view.activity.DetailTransaksiActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.jvm.java

class TransaksiRingkasAdapter : ListAdapter<Transaksi, TransaksiRingkasAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaksi_ringkas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaksi = getItem(position)
        holder.bind(transaksi)

        // TAMBAHKAN ONCLICKLISTENER DI SINI
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailTransaksiActivity::class.java).apply {
                putExtra("TRANSACTION_DATA", transaksi) // Kirim seluruh objek transaksi
            }
            context.startActivity(intent)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Panggil ID yang benar dari item_transaksi_ringkas.xml
        private val title: TextView = itemView.findViewById(R.id.text_title_transaksi)
        private val date: TextView = itemView.findViewById(R.id.text_date_transaksi)
        private val amount: TextView = itemView.findViewById(R.id.text_amount_transaksi)

        private val dateFormat = SimpleDateFormat("dd MMM yyyy • HH:mm", Locale("id", "ID"))
        private val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        fun bind(transaksi: Transaksi) {
            // Gunakan data dari objek Transaksi, bukan Jadwal
            title.text = "Setoran ${transaksi.items.firstOrNull()?.namaMaterial ?: "Sampah"}"
            date.text = transaksi.tanggal?.let { dateFormat.format(it) } ?: "Tanggal tidak tersedia"
            amount.text = "+${formatRupiah.format(transaksi.totalPoin)}"
        }
    }
    // ------------------------------------

    class DiffCallback : DiffUtil.ItemCallback<Transaksi>() {
        override fun areItemsTheSame(oldItem: Transaksi, newItem: Transaksi): Boolean {
            return oldItem.tanggal == newItem.tanggal
        }
        override fun areContentsTheSame(oldItem: Transaksi, newItem: Transaksi): Boolean {
            return oldItem == newItem
        }
    }
}