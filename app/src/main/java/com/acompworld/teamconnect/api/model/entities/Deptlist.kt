package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Deptlist(
    @Json(name = "department")
    val department: String="",
    @Json(name = "department_hin")
    val departmentHin: String="",
    @Json(name = "deptid")
    val deptid: Int
):Serializable