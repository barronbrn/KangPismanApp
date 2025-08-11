package com.example.kangpismanapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kangpismanapp.R
import com.example.kangpismanapp.data.model.BankSampah

class LokasiAdapter(
    private val onItemClick: (BankSampah) -> Unit
) : ListAdapter<BankSampah, LokasiAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lokasi_bank, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bankSampah = getItem(position)
        holder.bind(bankSampah)

        // Atur listener untuk setiap item
        holder.itemView.setOnClickListener {
            onItemClick(bankSampah)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image_bank_sampah)
        private val nama: TextView = itemView.findViewById(R.id.text_nama_bank_sampah)
        private val alamat: TextView = itemView.findViewById(R.id.text_alamat_bank_sampah)
        private val jarak: TextView = itemView.findViewById(R.id.text_jarak)
        private val jamBuka: TextView = itemView.findViewById(R.id.text_jam_buka)

        fun bind(bankSampah: BankSampah) {
            nama.text = bankSampah.nama
            alamat.text = bankSampah.alamat
            jarak.text = "%.1f km".format(bankSampah.jarakInKm)
            jamBuka.text = "Buka • Tutup 20:00" // Statis

            Glide.with(itemView.context)
                .load(bankSampah.imageUrl) // Ambil URL dari data
                .placeholder(R.drawable.ic_lokasi_bank) // Gambar default saat loading
                .error(R.drawable.ic_lokasi_bank) // Gambar default jika error
                .into(imageView)
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