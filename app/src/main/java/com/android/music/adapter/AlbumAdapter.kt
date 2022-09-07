package com.android.music.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.music.R
import com.android.music.databinding.ItemAlbumBinding
import com.android.music.model.Album

class AlbumAdapter(
    val itemAlbumListener: ItemAlbumListener
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>(){

    private var albums: List<Album> = listOf()

    inner class AlbumViewHolder(
        private var binding: ItemAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            val imageView = binding.imgAlbum
            val imgUri = album.link.toUri()
            imageView.load(imgUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
            binding.tvNameAlbum.text = album.name
            binding.tvNameSinger.text = album.singer_name
            binding.root.setOnClickListener{
                itemAlbumListener.onItemAlbumClick(album)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(ItemAlbumBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    fun setAlbums(albums: List<Album>) {
        this.albums = albums
        notifyDataSetChanged()
    }

    interface ItemAlbumListener {
        fun onItemAlbumClick(album: Album)
    }

}