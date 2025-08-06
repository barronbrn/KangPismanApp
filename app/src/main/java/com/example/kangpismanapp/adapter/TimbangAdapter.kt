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
import java.text.NumberFormat
import java.util.Locale

class TimbangAdapter : ListAdapter<ItemTransaksi, TimbangAdapter.TimbangViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimbangViewHolder {
        return TimbangViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_timbang, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimbangViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TimbangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaMaterial: TextView = itemView.findViewById(R.id.text_nama_material)
        private val detailTimbang: TextView = itemView.findViewById(R.id.text_detail_timbang)
        private val subtotal: TextView = itemView.findViewById(R.id.text_subtotal)
        private val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        fun bind(item: ItemTransaksi) {
            namaMaterial.text = item.namaMaterial
            detailTimbang.text = "${item.beratKg} kg x ${formatRupiah.format(item.hargaPerKg)}"
            subtotal.text = formatRupiah.format(item.subtotal)
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