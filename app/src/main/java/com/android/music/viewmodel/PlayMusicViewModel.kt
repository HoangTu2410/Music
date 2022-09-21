package com.android.music.viewmodel

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.music.model.Song
import com.android.music.network.MusicsAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

class PlayMusicViewModel : ViewModel() {

    private var _listSongPlay = MutableLiveData<List<Song>>()
    val listSongPlay: LiveData<List<Song>> = _listSongPlay
    private var _song = MutableLiveData<Song>()
    val song: LiveData<Song> = _song
    private var _duration = MutableLiveData<Int>()
    val duration: LiveData<Int> = _duration
    private var _currentTime = MutableLiveData<Int>()
    var currentTime: MutableLiveData<Int> = _currentTime
    private var mediaPlayer: MediaPlayer? = null
    private var position = 0

    fun loadSong(position: Int) {
        this.position = position
        _song.value = _listSongPlay.value?.get(position)
    }

    fun isPlayingMusic(): Boolean {
        return if (mediaPlayer == null) {
            false;
        } else {
            mediaPlayer!!.isPlaying
        }
    }

    suspend fun playMusic() {
        if (isPlayingMusic()) {
            stopMusic()
        }
        val url: String? = _song.value?.link

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                prepare()
                start()
            }
        } catch (e: Exception) {

        }

        _duration.postValue(mediaPlayer!!.duration)
        viewModelScope.launch {
            while (true) {
                if (mediaPlayer == null) break
                _currentTime.postValue(mediaPlayer!!.currentPosition)
                delay(1000)
            }
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    fun resumeMusic() {
        mediaPlayer?.start()
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun seekTo(position: Int) {
        mediaPlayer?.let {
            if (abs(position-mediaPlayer!!.currentPosition)>4) {
                mediaPlayer!!.seekTo(position)
            }
        }
    }

    fun nextMusic() {
        if (position == (_listSongPlay.value?.size?.minus(1) ?: 0)) {
            loadSong(0)
        } else {
            loadSong(position + 1)
        }
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            playMusic()
        }
    }

    fun previousMusic() {
        if (position == 0) {
            _listSongPlay.value?.size?.let {
                loadSong(it-1)
            }
        } else {
            loadSong(position - 1)
        }
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            playMusic()
        }
    }

    fun loadListNewSong() {
        viewModelScope.launch {
            try {
                _listSongPlay.value = MusicsAPI.retrofitService.getNewSongs()
            } catch (e: Exception) {
                _listSongPlay.value = listOf()
            }
        }
    }

    fun loadListSongBySinger(id_singer: Int) {
        viewModelScope.launch {
            try {
                _listSongPlay.value = MusicsAPI.retrofitService.getSongsBySinger(id_singer)
            } catch (e: Exception) {
                _listSongPlay.value = listOf()
            }
        }
    }

    fun loadListSongByAlbum(id_album: Int) {
        viewModelScope.launch {
            try {
                _listSongPlay.value = MusicsAPI.retrofitService.getSongsByAlbum(id_album)
            } catch (e: Exception) {
                _listSongPlay.value = listOf()
            }
        }
    }

}