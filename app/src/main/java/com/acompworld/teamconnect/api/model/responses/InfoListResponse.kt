package com.acompworld.teamconnect.api.model.responses


import com.acompworld.teamconnect.api.model.entities.FeedItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InfoListResponse(
    @Json(name = "data")
    val list : List<FeedItem>?,
    @Json(name = "type")
    val type: String?
)