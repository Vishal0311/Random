package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Empdirectory(
    @Json(name = "department")
    val department: String?,
    @Json(name = "designation")
    val designation: String?,
    @Json(name = "empname")
    val empname: String,
    @Json(name = "empno")
    val empno: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "profilepic")
    val profilepic: String="",
    @Json(name = "type")
    val type: String
)