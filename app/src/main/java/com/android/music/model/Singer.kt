package com.android.music.model

import com.squareup.moshi.Json

data class Singer(
    val id: Int,
    val name: String,
    @Json(name = "image") val link: String
)