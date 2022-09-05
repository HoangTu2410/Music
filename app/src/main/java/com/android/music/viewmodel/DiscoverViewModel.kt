package com.android.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.music.model.Album
import com.android.music.model.Singer
import com.android.music.model.Song
import com.android.music.network.MusicsAPI
import kotlinx.coroutines.launch

enum class MusicsStatus { LOADING, ERROR, DONE }

class DiscoverViewModel : ViewModel() {

    private val _status = MutableLiveData<MusicsStatus>()
    val status: LiveData<MusicsStatus> = _status

    private val _singers = MutableLiveData<List<Singer>>()
    val singers: LiveData<List<Singer>> = _singers

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> = _albums

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs

    init {
        getSingers()
        getAlbums()
        getSongs()
    }

    private fun getSongs() {
        viewModelScope.launch {
            try {
                _songs.value = MusicsAPI.retrofitService.getNewSongs()
            } catch (e: Exception) {
                _songs.value = listOf()
            }
        }
    }

    private fun getAlbums() {
        viewModelScope.launch {
            try {
                _albums.value = MusicsAPI.retrofitService.getNewAlbums()
            } catch (e: Exception) {
                _albums.value = listOf()
            }
        }
    }

    private fun getSingers() {

        viewModelScope.launch {
            _status.value = MusicsStatus.LOADING
            try {
                _singers.value = MusicsAPI.retrofitService.getNewSingers()
                _status.value = MusicsStatus.DONE
            } catch (e: Exception) {
                _status.value = MusicsStatus.ERROR
                _singers.value = listOf()
            }
        }
    }
}