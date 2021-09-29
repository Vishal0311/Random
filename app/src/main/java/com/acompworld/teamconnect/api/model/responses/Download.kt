package com.acompworld.teamconnect.api.model.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Download(
    @Json(name = "filepath")
    val filepath: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String
)