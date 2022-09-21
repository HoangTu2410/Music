package com.android.music.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import coil.load
import com.android.music.R
import com.android.music.databinding.FragmentPlayMusicBinding
import com.android.music.viewmodel.PlayMusicViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.*

class PlayMusicFragment : Fragment() {
    private var _binding: FragmentPlayMusicBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayMusicViewModel by viewModels()
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = arguments?.getString("type")
        this.position = arguments?.getInt("position")
        val id = arguments?.getInt("id")
        when (type) {
            "NEW_SONGS" -> viewModel.loadListNewSong()
            "SONG_OF_ALBUM" -> viewModel.loadListSongByAlbum(id!!)
            "SONG_OF_SINGER" -> viewModel.loadListSongBySinger(id!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayMusicBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        position?.let { loadView(it) }

        AnimationUtils.loadAnimation(context,R.anim.anim_rotate).also {
            binding.imgSong.startAnimation(it)
        }

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

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewModel.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
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
                viewModel.duration.observe(viewLifecycleOwner) {
                    binding.seekBar.max = it
                    val minute: Int = it/60000
                    val second: Int = (it - minute*60000)/1000
                    if (second<10) {
                        binding.tvDuration.text = "${minute}:0${second}"
                    } else {
                        binding.tvDuration.text = "${minute}:${second}"
                    }
                }
                viewModel.currentTime.observe(viewLifecycleOwner) {
                    binding.seekBar.progress = it
                }
            }
            val coroutine = CoroutineScope(Dispatchers.Default)
            coroutine.launch {
                viewModel.playMusic()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}