package com.android.music.model

import com.squareup.moshi.Json

data class Singer(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "image") val link: String
)