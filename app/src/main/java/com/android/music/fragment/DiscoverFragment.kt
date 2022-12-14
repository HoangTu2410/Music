package com.android.music.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.music.R
import com.android.music.adapter.AlbumAdapter
import com.android.music.adapter.SingerAdapter
import com.android.music.adapter.SongAdapter
import com.android.music.databinding.FragmentDiscoverBinding
import com.android.music.model.Album
import com.android.music.model.Singer
import com.android.music.model.Song
import com.android.music.viewmodel.DiscoverViewModel

class DiscoverFragment : Fragment(), SingerAdapter.ItemSingerListener,
    AlbumAdapter.ItemAlbumListener, SongAdapter.ItemSongListener {
    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DiscoverViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapterSingers = SingerAdapter(this)
        binding.rcvNewSingers.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        binding.rcvNewSingers.adapter = adapterSingers
        binding.rcvNewSingers.setHasFixedSize(true)
        viewModel.singers.observe(viewLifecycleOwner) {
            adapterSingers.setSingers(it)
        }

        val adapterAlbums = AlbumAdapter(this)
        binding.rcvNewAlbums.layoutManager = GridLayoutManager(context,3,RecyclerView.HORIZONTAL,false)
        binding.rcvNewAlbums.adapter = adapterAlbums
        binding.rcvNewAlbums.setHasFixedSize(true)
        viewModel.albums.observe(viewLifecycleOwner) {
            adapterAlbums.setAlbums(it)
        }

        val adapterSongs = SongAdapter(this)
        binding.rcvSongs.layoutManager = LinearLayoutManager(context)
        binding.rcvSongs.adapter = adapterSongs
        binding.rcvSongs.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.rcvSongs.addItemDecoration(itemDecoration)
        viewModel.songs.observe(viewLifecycleOwner) {
            adapterSongs.setSongs(it)
        }

        binding.imgPersonal.setOnClickListener {
            parentFragment?.parentFragment?.findNavController()?.navigate(R.id.action_mainFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSingerClick(singer: Singer) {
        val bundle = Bundle()
        bundle.putInt("id_singer", singer.id)
        bundle.putString("image_singer", singer.link)
        parentFragment?.parentFragment?.findNavController()?.navigate(R.id.action_mainFragment_to_singerFragment,bundle)
    }

    override fun onItemAlbumClick(album: Album) {
        val bundle = Bundle()
        bundle.putInt("id_album", album.id)
        bundle.putString("image_album", album.link)
        parentFragment?.parentFragment?.findNavController()?.navigate(R.id.action_mainFragment_to_albumFragment,bundle)
    }

    override fun onItemSongClick(position: Int) {
        val bundle = Bundle()
        bundle.putString("type","NEW_SONGS")
        bundle.putInt("position", position)
        parentFragment?.parentFragment?.findNavController()?.navigate(R.id.action_mainFragment_to_playMusicFragment,bundle)
    }

}