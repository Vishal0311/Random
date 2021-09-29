package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Update(
    @Json(name = "description_eng")
    val descriptionEng: String?,
    @Json(name = "description_hin")
    val descriptionHin: String?,
    @Json(name = "image")
    val image: String?,
    @Json(name = "postedon")
    val postedon: String?
)