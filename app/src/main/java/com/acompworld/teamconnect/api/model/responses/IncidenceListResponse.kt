package com.acompworld.teamconnect.api.model.responses

import com.acompworld.teamconnect.api.model.entities.Data
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IncidenceListResponse(
    @Json(name = "data")
    val `data`: List<Data>
)
