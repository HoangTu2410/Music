package com.android.music.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.android.music.R
import com.android.music.adapter.SongAdapter
import com.android.music.databinding.FragmentListSongBinding
import com.android.music.viewmodel.ListSongViewModel

class ListSongFragment : Fragment() {

    private var _binding: FragmentListSongBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: ListSongViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListSongBinding.inflate(inflater, container, false)
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
        viewModel.listSong.observe(viewLifecycleOwner) {
            adapter.setSongs(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}