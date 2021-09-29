package com.acompworld.teamconnect.api.model.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "empno")
    val empno: String,
    @Json(name = "password")
    val password: String
)
