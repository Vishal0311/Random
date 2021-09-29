package com.acompworld.teamconnect.api.model.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "Downloads")
    val Downloads: List<Download>,
    @Json(name = "category")
    val category: String,
    @Json(name = "categoryid")
    val categoryid: Int
)