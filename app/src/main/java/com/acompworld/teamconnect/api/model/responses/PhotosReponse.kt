package com.acompworld.teamconnect.api.model.responses


import com.acompworld.teamconnect.api.model.entities.Photo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotosReponse(
    @Json(name = "albumid")
    val albumid: Int?,
    @Json(name = "albumname")
    val name: String?,
    @Json(name = "photos")
    val photos: List<Photo>?,
    @Json(name = "year")
    val year: Int?
)