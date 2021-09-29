package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Bannerlist(
    @Json(name = "bannerfile")
    val bannerfile: String?,
    @Json(name = "captioneng")
    val captionEng: String?,
    @Json(name = "captionhin")
    val captionHin: String?,
    @Json(name = "postedon")
    val postedon: String?
)