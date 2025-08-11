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
import com.example.kangpismanapp.data.model.Artikel

class ArtikelAdapter (
    private val onItemClick: (Artikel) -> Unit
) : ListAdapter<Artikel, ArtikelAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artikel_full, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artikel = getItem(position)
        holder.bind(artikel)
        holder.itemView.setOnClickListener {
            onItemClick(artikel)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image_artikel)
        private val title: TextView = itemView.findViewById(R.id.text_title_artikel)
        private val description: TextView = itemView.findViewById(R.id.text_desc_artikel)

        fun bind(artikel: Artikel) {
            title.text = artikel.title
            description.text = artikel.description

            Glide.with(itemView.context)
                .load(artikel.imageUrl)
                .placeholder(R.drawable.ic_edukasi) // Gambar placeholder
                .error(R.drawable.ic_edukasi)       // Gambar jika URL error
                .into(imageView)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Artikel>() {
        override fun areItemsTheSame(oldItem: Artikel, newItem: Artikel): Boolean {
            // Asumsikan judul artikel unik
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Artikel, newItem: Artikel): Boolean {
            return oldItem == newItem
        }
    }
}