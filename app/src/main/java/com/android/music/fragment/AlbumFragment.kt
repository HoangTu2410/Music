package com.android.music.fragment

import android.os.Bundle
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
import com.android.music.databinding.FragmentAlbumBinding
import com.android.music.model.Song
import com.android.music.viewmodel.SongsViewModel

class AlbumFragment : Fragment(), SongAdapter.ItemSongListener {
    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SongsViewModel by viewModels()
    private var id_album: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        id_album = arguments?.getInt("id_album")
        val image_album = arguments?.getString("image_album")
        val imageView = binding.imgAlbum
        val imgUri = image_album?.toUri()
        imageView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
        viewModel.loadListSongByAlbum(id_album!!)
        val adapter = SongAdapter(this)
        binding.rcvListSong.layoutManager = LinearLayoutManager(context)
        binding.rcvListSong.adapter = adapter
        binding.rcvListSong.setHasFixedSize(true)
        viewModel.songsOfAlbum.observe(viewLifecycleOwner) {
            adapter.setSongs(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSongClick(position: Int) {
        val bundle = Bundle()
        bundle.putString("type","SONG_OF_ALBUM")
        bundle.putInt("position", position)
        bundle.putInt("id", id_album!!)
        parentFragment?.findNavController()?.navigate(R.id.action_albumFragment_to_playMusicFragment,bundle)
    }

}