package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Teldirectory(
    @Json(name = "category")
    val category: String="",

    @Json(name = "icon")
    val icon: String="",

    @Json(name = "id")
    val id: Int,
    @Json(name = "location")
    val location: String="",
    
    @Json(name = "type")
    val type: String
)