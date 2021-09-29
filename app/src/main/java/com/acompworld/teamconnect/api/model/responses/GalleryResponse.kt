package com.acompworld.teamconnect.api.model.responses


import com.acompworld.teamconnect.api.model.entities.Album
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GalleryResponse(
    @Json(name = "albums")
    val albums: List<Album> = listOf<Album>(),
    @Json(name = "year")
    val year: Int?,

    @Json(name = "message")
    val message: String?,
    @Json(name = "status")
    val status: String?
)