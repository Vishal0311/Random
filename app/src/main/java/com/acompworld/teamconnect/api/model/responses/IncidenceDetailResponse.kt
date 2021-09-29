package com.acompworld.teamconnect.api.model.responses

import com.acompworld.teamconnect.api.model.entities.IncidenceDetail
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IncidenceDetailResponse(
    @Json(name = "data")
    val `data`: IncidenceDetail,
    @Json(name = "status")
    val status: String
)
