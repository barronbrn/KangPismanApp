package com.example.kangpismanapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.data.model.BankSampah

class LokasiAdapter : ListAdapter<BankSampah, LokasiAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lokasi_bank, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bankSampah = getItem(position)
        holder.bind(bankSampah)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nama: TextView = itemView.findViewById(R.id.text_nama_bank_sampah)
        private val alamat: TextView = itemView.findViewById(R.id.text_alamat_bank_sampah)
        private val jarak: TextView = itemView.findViewById(R.id.text_jarak)
        private val jamBuka: TextView = itemView.findViewById(R.id.text_jam_buka)

        fun bind(bankSampah: BankSampah) {
            nama.text = bankSampah.nama
            alamat.text = bankSampah.alamat
            // Format jarak agar hanya menampilkan 1 angka di belakang koma
            jarak.text = "%.1f km".format(bankSampah.jarakInKm)

            // Untuk saat ini, jam buka kita buat statis.
            // Nantinya bisa ditambahkan sebagai field di Firestore.
            jamBuka.text = "Buka • Tutup 20:00"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<BankSampah>() {
        override fun areItemsTheSame(oldItem: BankSampah, newItem: BankSampah): Boolean {
            return oldItem.nama == newItem.nama // Asumsikan nama bank sampah unik
        }

        override fun areContentsTheSame(oldItem: BankSampah, newItem: BankSampah): Boolean {
            return oldItem == newItem
        }
    }
}