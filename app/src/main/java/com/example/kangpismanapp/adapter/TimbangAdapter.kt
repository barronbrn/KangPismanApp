package com.example.kangpismanapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.data.model.ItemTransaksi

class TimbangAdapter : ListAdapter<ItemTransaksi, TimbangAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timbang, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaMaterial: TextView = itemView.findViewById(R.id.text_nama_material)
        private val hargaPerKg: TextView = itemView.findViewById(R.id.text_harga_per_kg)
        private val beratEstimasi: TextView = itemView.findViewById(R.id.text_berat_estimasi)

        fun bind(item: ItemTransaksi) {
            namaMaterial.text = item.namaMaterial
            hargaPerKg.text = "Rp ${item.hargaPerKg}/kg"
            beratEstimasi.text = "${item.beratKg} kg"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ItemTransaksi>() {
        override fun areItemsTheSame(oldItem: ItemTransaksi, newItem: ItemTransaksi): Boolean {
            return oldItem.namaMaterial == newItem.namaMaterial
        }
        override fun areContentsTheSame(oldItem: ItemTransaksi, newItem: ItemTransaksi): Boolean {
            return oldItem == newItem
        }
    }
}