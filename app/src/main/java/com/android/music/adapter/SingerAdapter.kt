package com.android.music.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.music.R
import com.android.music.databinding.ItemSingerBinding
import com.android.music.model.Singer

class SingerAdapter(
    var itemSingerListener: ItemSingerListener
) : RecyclerView.Adapter<SingerAdapter.SingerViewHolder>() {

    private var singers: List<Singer> = listOf()

    inner class SingerViewHolder(
        private var binding: ItemSingerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(singer: Singer) {
            val imageView = binding.imgSinger
            val imgUri = singer.link.toUri()
            imageView.load(imgUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
            binding.tvNameSinger.text = singer.name
            binding.root.setOnClickListener{
                itemSingerListener.onItemSingerClick(singer)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingerViewHolder {
        return SingerViewHolder(ItemSingerBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SingerViewHolder, position: Int) {
        holder.bind(singers[position])
    }

    override fun getItemCount(): Int {
        return singers.size
    }

    fun setSingers(singers: List<Singer>) {
        this.singers = singers
        notifyDataSetChanged()
    }

    interface ItemSingerListener {
        fun onItemSingerClick(singer: Singer)
    }
}