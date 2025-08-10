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

class ArtikelHomeAdapter(
    private val onItemClick: (Artikel) -> Unit
) : ListAdapter<Artikel, ArtikelHomeAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artikel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artikel = getItem(position)
        // 2. Kirim artikel dan fungsi klik ke ViewHolder
        holder.bind(artikel, onItemClick)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image_artikel_home)
        private val title: TextView = itemView.findViewById(R.id.text_title_artikel_home)

        // 3. Tambahkan listener pada itemView
        fun bind(artikel: Artikel, onItemClick: (Artikel) -> Unit) {
            title.text = artikel.title
            Glide.with(itemView.context).load(artikel.imageUrl).into(imageView)

            itemView.setOnClickListener {
                onItemClick(artikel) // Jalankan fungsi saat item di-klik
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Artikel>() {
        override fun areItemsTheSame(oldItem: Artikel, newItem: Artikel): Boolean {
            return oldItem.title == newItem.title
        }
        override fun areContentsTheSame(oldItem: Artikel, newItem: Artikel): Boolean {
            return oldItem == newItem
        }
    }
}