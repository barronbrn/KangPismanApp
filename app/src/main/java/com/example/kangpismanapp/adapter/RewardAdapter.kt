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
import com.example.kangpismanapp.data.model.Reward


class RewardAdapter(
    private val onItemClick: (Reward) -> Unit
) : ListAdapter<Reward, RewardAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reward, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reward = getItem(position)
        holder.bind(reward)


        holder.itemView.setOnClickListener {
            onItemClick(reward)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image_reward)
        private val title: TextView = itemView.findViewById(R.id.text_reward_title)
        private val description: TextView = itemView.findViewById(R.id.text_reward_description)
        private val points: TextView = itemView.findViewById(R.id.text_reward_points)


        fun bind(reward: Reward) {
            title.text = reward.title
            description.text = reward.description
            points.text = "${reward.points} pts"

            Glide.with(itemView.context)
                .load(reward.imageUrl)
                .placeholder(R.drawable.ic_reward) // Gunakan ikon yang relevan
                .error(R.drawable.ic_reward)
                .into(imageView)
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<Reward>() {
        override fun areItemsTheSame(oldItem: Reward, newItem: Reward): Boolean {
            // Bandingkan dengan properti yang unik, misalnya title
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Reward, newItem: Reward): Boolean {
            return oldItem == newItem
        }
    }
}