package com.example.mydicodingevent.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydicodingevent.data.local.entity.EventEntity
import com.example.mydicodingevent.databinding.ItemRowEventBinding
import com.example.mydicodingevent.ui.detailevent.DetailEventActivity

class FavoriteAdapter(private var favorites: List<EventEntity>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(private val binding: ItemRowEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            binding.nameEvent.text = event.title
            Glide.with(binding.root).load(event.imageUrl).into(binding.imgView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorites[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailEventActivity::class.java)
            intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, favorites[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = favorites.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newFavorites: List<EventEntity>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }
}