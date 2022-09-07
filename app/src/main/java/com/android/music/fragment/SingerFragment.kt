package com.android.music.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.music.R
import com.android.music.adapter.SongAdapter
import com.android.music.databinding.FragmentSingerBinding
import com.android.music.viewmodel.SongsViewModel

class SingerFragment : Fragment() {

    private var _binding: FragmentSingerBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: SongsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingerBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        val id_singer = arguments?.getInt("id_singer")
        val image_singer = arguments?.getString("image_singer")
        val imageView = binding.imgSinger
        val imgUri = image_singer?.toUri()
        imageView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
        if (id_singer != null) {
            viewModel.loadListSongBySinger(id_singer)
        }
        val adapter = SongAdapter()
        binding.rcvListSong.layoutManager = LinearLayoutManager(context)
        binding.rcvListSong.adapter = adapter
        binding.rcvListSong.setHasFixedSize(true)
        viewModel.songsOfSinger.observe(viewLifecycleOwner) {
            adapter.setSongs(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}