package com.acompworld.teamconnect.api.model.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class IncidenceReportRequest(
    @Json(name = "area")
    val area: String?,
    @Json(name = "date")
    val date: String?,
    @Json(name = "details")
    val details: String?,
    @Json(name = "incidencetitle")
    val incidencetitle: String?,
    @Json(name = "photos")
    val photos: List<Photo>?,
    @Json(name = "projcode")
    val projcode: String?,
    @Json(name = "time")
    val time: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "userid")
    val userid: String?
)
