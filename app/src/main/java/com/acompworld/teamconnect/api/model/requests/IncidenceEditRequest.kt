package com.acompworld.teamconnect.api.model.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IncidenceEditRequest(
    @Json(name = "incidenceid")
    val incidenceid:Int?,
    @Json(name = "incidencetitle")
    val incidencetitle:String?,
    @Json(name = "date")
    val date:String?,
    @Json(name = "time")
    val time:String?,
    @Json(name = "area")
    val area:String?,
    @Json(name = "type")
    val type:String?,
    @Json(name = "details")
    val details:String?
)
