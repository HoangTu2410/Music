package com.android.music.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.music.R
import com.android.music.databinding.ItemSongBinding
import com.android.music.model.Song

class SongAdapter(
    val itemSongListener: ItemSongListener
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var songs: List<Song> = listOf()

    inner class SongViewHolder(
        private var binding: ItemSongBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song, position: Int) {
            val imageView = binding.imgSong
            val imgUri = song.image.toUri()
            imageView.load(imgUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
            binding.tvNumber.text = (position+1).toString()
            binding.tvNameSong.text = song.name
            binding.tvNameSinger.text = song.singer_name
            binding.root.setOnClickListener{
                itemSongListener.onItemSongClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(ItemSongBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position],position)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun setSongs(songs: List<Song>) {
        this.songs = songs
        notifyDataSetChanged()
    }

    interface ItemSongListener {
        fun onItemSongClick(position: Int)
    }
}