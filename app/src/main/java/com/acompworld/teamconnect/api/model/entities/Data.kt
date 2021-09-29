package com.acompworld.teamconnect.api.model.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    val Area: String,
    val Details: String,
    val IncidenceID: Int,
    val IncidenceTitle: String,
    val ProjCode: String,
    val Time: String,
    val Type: String,
    val date: String
)
