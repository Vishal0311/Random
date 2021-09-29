package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Generationdata(
    @Json(name = "freq")
    val freq: String,
    @Json(name = "generation")
    val generation: String,
    @Json(name = "plf")
    val plf: String,
    @Json(name = "runningunits")
    val runningunits: String,
    @Json(name = "totalunits")
    val totalunits: String
)