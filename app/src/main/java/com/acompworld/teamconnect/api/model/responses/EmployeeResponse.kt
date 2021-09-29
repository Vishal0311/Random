package com.acompworld.teamconnect.api.model.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeResponse(
    @Json(name = "alternatemobile")
    val alternatemobile: String,
    @Json(name = "department")
    val department: String,
    @Json(name = "designation")
    val designation: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "empname")
    val empname: String,
    @Json(name = "empno")
    val empno: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "intercomoffice")
    val intercomoffice: String,
    @Json(name = "Level")
    val level: String,
    @Json(name = "location")
    val location: String,
    @Json(name = "Mobile")
    val mobile: String,
    @Json(name = "profilepic")
    val profilepic: String
)