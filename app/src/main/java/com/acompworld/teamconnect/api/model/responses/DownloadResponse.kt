package com.acompworld.teamconnect.api.model.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class DownloadResponse(
    @Json(name = "data")
    val `data`: List<Data>,
    @Json(name = "message")
    val message: String,
    @Json(name = "status")
    val status: String
)