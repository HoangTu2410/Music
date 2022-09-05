package com.android.music.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.music.R
import com.android.music.adapter.AlbumAdapter
import com.android.music.adapter.SingerAdapter
import com.android.music.adapter.SongAdapter
import com.android.music.databinding.FragmentDiscoverBinding
import com.android.music.viewmodel.DiscoverViewModel

class DiscoverFragment : Fragment() {
    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DiscoverViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapterSingers = SingerAdapter()
        binding.rcvNewSingers.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        binding.rcvNewSingers.adapter = adapterSingers
        viewModel.singers.observe(viewLifecycleOwner) {
            adapterSingers.setSingers(it)
        }

        val adapterAlbums = AlbumAdapter()
        binding.rcvNewAlbums.layoutManager = GridLayoutManager(context,3,RecyclerView.HORIZONTAL,false)
        binding.rcvNewAlbums.adapter = adapterAlbums
        viewModel.albums.observe(viewLifecycleOwner) {
            adapterAlbums.setAlbums(it)
        }

        val adapterSongs = SongAdapter()
        binding.rcvSongs.layoutManager = LinearLayoutManager(context)
        binding.rcvSongs.adapter = adapterSongs
        viewModel.songs.observe(viewLifecycleOwner) {
            adapterSongs.setSongs(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}