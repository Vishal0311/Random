package com.acompworld.teamconnect.api.model


import com.acompworld.teamconnect.api.model.entities.FeedItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "data")
    val data: List<FeedItem>?
)