package com.android.music.model

import com.squareup.moshi.Json

data class Album(
    val id: Int,
    val name: String,
    @Json(name = "image") val link: String,
    val singer_name: String
)
