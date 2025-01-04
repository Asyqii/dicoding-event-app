package com.example.mydicodingevent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydicodingevent.R
import com.example.mydicodingevent.data.local.entity.EventEntity
import com.example.mydicodingevent.data.response.ListEventsItem
import com.example.mydicodingevent.databinding.ItemRowEventBinding

class ReviewAdapter(
    private val onClick: (ListEventsItem) -> Unit,
) : ListAdapter<ListEventsItem, ReviewAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    class MyViewHolder(
        private val binding: ItemRowEventBinding,
        private val onClick: (ListEventsItem) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.nameEvent.text = event.summary
            Glide.with(binding.imgView.context).load(event.mediaCover).into(binding.imgView)
            binding.root.setOnClickListener { onClick(event) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)


    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {

            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ) : Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ) : Boolean {
                return oldItem == newItem
            }

        }
    }


    }




