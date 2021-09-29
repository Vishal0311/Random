package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Latesupdate(
    @Json(name = "attachmentpath")
    val attachmentpath: String?,
    @Json(name = "LinkURL")
    val linkURL: String?,
    @Json(name = "PostedOn")
    val postedOn: String?,
    @Json(name = "postedby")
    val postedby: String?,
    @Json(name = "TitleEnglish")
    val titleEnglish: String?,
    @Json(name = "TitleHindi")
    val titleHindi: String?,
    @Json(name = "Type")
    val type: String?
)