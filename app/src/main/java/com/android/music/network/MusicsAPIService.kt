package com.android.music.network

import com.android.music.model.Album
import com.android.music.model.Singer
import com.android.music.model.Song
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://music-2410.000webhostapp.com/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MusicsAPIService {
    @GET("new_singers.php")
    suspend fun getNewSingers(): List<Singer>
    @GET("new_albums.php")
    suspend fun getNewAlbums(): List<Album>
    @GET("new_songs.php")
    suspend fun getNewSongs(): List<Song>
    @GET("songs.php")
    suspend fun getSongsBySinger(@Query("id_singer") id_singer: Int): List<Song>
    @GET("songs.php")
    suspend fun getSongsByAlbum(@Query("id_album") id_album: Int): List<Song>
}

object MusicsAPI {
    val retrofitService: MusicsAPIService by lazy { retrofit.create(MusicsAPIService::class.java) }
}