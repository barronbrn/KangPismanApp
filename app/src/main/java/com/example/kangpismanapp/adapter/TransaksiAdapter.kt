package com.example.kangpismanapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.data.model.Transaksi
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class TransaksiAdapter(
    private val onItemClick: (Transaksi) -> Unit
) : ListAdapter<Transaksi, TransaksiAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaksi_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaksi = getItem(position)
        holder.bind(transaksi)
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onItemClick(getItem(position)) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.text_transaction_title)
        private val date: TextView = itemView.findViewById(R.id.text_transaction_date)
        private val amount: TextView = itemView.findViewById(R.id.text_transaction_amount)

        private val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
        private val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        fun bind(transaksi: Transaksi) {
            val itemCount = transaksi.items.size
            title.text = "Transaksi dengan $itemCount item"
            date.text = transaksi.tanggal?.let { dateFormat.format(it) } ?: "Tanggal tidak tersedia"
            amount.text = "+${formatRupiah.format(transaksi.totalRupiah)}"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Transaksi>() {
        override fun areItemsTheSame(oldItem: Transaksi, newItem: Transaksi): Boolean {
            return oldItem.tanggal == newItem.tanggal && oldItem.totalPoin == newItem.totalPoin
        }

        override fun areContentsTheSame(oldItem: Transaksi, newItem: Transaksi): Boolean {
            return oldItem == newItem
        }
    }
}