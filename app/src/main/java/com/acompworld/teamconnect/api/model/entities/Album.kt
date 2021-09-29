package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Album(
    @Json(name = "albumid")
    val albumid: Int?,
    @Json(name = "firstphotopath")
    val firstphotopath: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "postedon")
    val postedon: String?,
    @Json(name = "totalphotos")
    val totalphotos: Int?,

    @Json(name = "message")
    val message: String?,
    @Json(name = "status")
    val status: String?
)