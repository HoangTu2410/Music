package com.android.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.music.model.Song
import com.android.music.network.MusicsAPI
import kotlinx.coroutines.launch

class ListSongViewModel : ViewModel() {

    private val _listSong = MutableLiveData<List<Song>>()
    val listSong: LiveData<List<Song>> = _listSong

    fun loadListSongBySinger(id_singer: Int) {
        viewModelScope.launch {
            try {
                _listSong.value = MusicsAPI.retrofitService.getSongsBySinger(id_singer)
            } catch (e: Exception) {
                _listSong.value = listOf()
            }
        }
    }

}