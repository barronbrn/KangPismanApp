package com.example.kangpismanapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.data.model.Sampah
import java.text.NumberFormat
import java.util.Locale


class HargaAdapter : ListAdapter<Sampah, HargaAdapter.HargaViewHolder>(SampahDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HargaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_harga, parent, false)
        return HargaViewHolder(view)
    }

    override fun onBindViewHolder(holder: HargaViewHolder, position: Int) {
        val sampah = getItem(position)
        holder.bind(sampah)
    }

    class HargaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaMaterial: TextView = itemView.findViewById(R.id.text_nama_material)
        private val hargaMaterial: TextView = itemView.findViewById(R.id.text_harga_material)

        fun bind(sampah: Sampah) {
            namaMaterial.text = sampah.nama

            val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

            formatRupiah.maximumFractionDigits = 0
            hargaMaterial.text = "${formatRupiah.format(sampah.harga)}/kg"
        }
    }
}

class SampahDiffCallback : DiffUtil.ItemCallback<Sampah>() {
    override fun areItemsTheSame(oldItem: Sampah, newItem: Sampah): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Sampah, newItem: Sampah): Boolean {
        return oldItem == newItem
    }
}
