package com.android.music.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.music.model.Song
import com.android.music.network.MusicsAPI
import kotlinx.coroutines.launch

class SongsViewModel : ViewModel() {

    private val _songsOfSinger = MutableLiveData<List<Song>>()
    val songsOfSinger: LiveData<List<Song>> = _songsOfSinger

    private val _songsOfAlbum = MutableLiveData<List<Song>>()
    val songsOfAlbum: LiveData<List<Song>> = _songsOfAlbum

    fun loadListSongBySinger(id_singer: Int) {
        viewModelScope.launch {
            try {
                _songsOfSinger.value = MusicsAPI.retrofitService.getSongsBySinger(id_singer)
            } catch (e: Exception) {
                _songsOfSinger.value = listOf()
            }
        }
    }

    fun loadListSongByAlbum(id_album: Int) {
        viewModelScope.launch {
            try {
                _songsOfAlbum.value = MusicsAPI.retrofitService.getSongsByAlbum(id_album)
            } catch (e: Exception) {
                _songsOfAlbum.value = listOf()
            }
        }
    }

}