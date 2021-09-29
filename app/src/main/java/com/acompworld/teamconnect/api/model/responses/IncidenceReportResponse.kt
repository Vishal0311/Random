package com.acompworld.teamconnect.api.model.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IncidenceReportResponse(
    @Json(name = "message")
    val message: String,
    @Json(name = "status")
    val status: String
)