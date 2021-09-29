package com.acompworld.teamconnect.api.model.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "data")
    val data: LoginResponseData,
    override val status: String,
    override val message: String
) : MasterResponse()
@JsonClass(generateAdapter = true)
data class LoginResponseData(
    @Json(name = "userid")
    val userid: Int,
    @Json(name = "empno")
    val empno: String,
    @Json(name = "empname")
    val empname:String,
    @Json(name = "projectcode")
    val projectCode:String,
    @Json(name = "projectname")
    val projectname:String,
    @Json(name = "department")
    val department: String,
    @Json(name = "designation")
    val designation: String
)