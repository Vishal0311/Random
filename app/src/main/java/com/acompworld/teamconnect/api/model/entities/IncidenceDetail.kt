package com.acompworld.teamconnect.api.model.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IncidenceDetail(
    val area: String,
    val date: String,
    val details: String,
    val incidenceid: Int,
    val photos: List<Photo>,
    val time: String,
    val title: String,
    val type: String
)
