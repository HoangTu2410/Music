package com.android.music.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import coil.load
import com.android.music.R
import com.android.music.databinding.FragmentPlayMusicBinding
import com.android.music.viewmodel.PlayMusicViewModel

class PlayMusicFragment : Fragment() {
    private var _binding: FragmentPlayMusicBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayMusicViewModel by viewModels()
    private var position: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayMusicBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.getString("type")
        this.position = arguments?.getInt("position")
        val id = arguments?.getInt("id")
        when (type) {
            "NEW_SONGS" -> viewModel.loadListNewSong()
            "SONG_OF_ALBUM" -> viewModel.loadListSongByAlbum(id!!)
            "SONG_OF_SINGER" -> viewModel.loadListSongBySinger(id!!)
        }
        position?.let { loadView(it) }

        binding.iconPausePlay.setOnClickListener {
            if (viewModel.isPlayingMusic()) {
                binding.iconPausePlay.setImageResource(R.drawable.ic_play)
                viewModel.pauseMusic()
            } else {
                binding.iconPausePlay.setImageResource(R.drawable.ic_pause)
                viewModel.resumeMusic()
            }
        }

        binding.icSkipNext.setOnClickListener {
            viewModel.nextMusic()
        }

        binding.icSkipPrevious.setOnClickListener {
            viewModel.previousMusic()
        }
    }

    private fun loadView(position: Int) {
        viewModel.listSongPlay.observe(viewLifecycleOwner) {
            viewModel.loadSong(position)
            viewModel.song.observe(viewLifecycleOwner) {
                binding.tvNameSong.text = it.name
                binding.tvNameSinger.text = it.singer_name
                val imageView = binding.imgSong
                val imgUri = it.image.toUri()
                imageView.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
                AnimationUtils.loadAnimation(context,R.anim.anim_rotate).also {
                    imageView.startAnimation(it)
                }
                viewModel.playMusic()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}