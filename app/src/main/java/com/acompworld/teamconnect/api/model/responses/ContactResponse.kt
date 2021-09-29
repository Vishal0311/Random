package com.acompworld.teamconnect.api.model.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContactResponse(
    @Json(name = "address")
    val address: String,
    @Json(name = "category")
    val category: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "intercome")
    val intercome: String,
    @Json(name = "location")
    val location: String,
    @Json(name = "phone")
    val phone: String
)