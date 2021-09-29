package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photo(
    @Json(name = "photoid")
    val photoid: Int?,
    @Json(name = "photopath")
    val photopath: String?
)