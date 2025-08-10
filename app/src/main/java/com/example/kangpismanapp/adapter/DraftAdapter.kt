package com.example.kangpismanapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.data.model.ItemDraft

class DraftAdapter(
    private val onDeleteClick: (ItemDraft) -> Unit
) : ListAdapter<ItemDraft, DraftAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_draft, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onDeleteClick)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaMaterial: TextView = itemView.findViewById(R.id.text_nama_material_draft)
        private val berat: TextView = itemView.findViewById(R.id.text_berat_draft) // <-- Tambahkan ini
        private val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete_draft_item)

        fun bind(item: ItemDraft, onDeleteClick: (ItemDraft) -> Unit) {
            namaMaterial.text = item.namaMaterial
            berat.text = "Estimasi: ${item.estimasiBeratKg} kg"
            deleteButton.setOnClickListener { onDeleteClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ItemDraft>() {
        override fun areItemsTheSame(oldItem: ItemDraft, newItem: ItemDraft): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ItemDraft, newItem: ItemDraft): Boolean {
            return oldItem == newItem
        }
    }
}