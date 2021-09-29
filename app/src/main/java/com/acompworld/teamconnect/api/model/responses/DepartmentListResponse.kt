package com.acompworld.teamconnect.api.model.responses


import com.acompworld.teamconnect.api.model.entities.Deptlist
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DepartmentListResponse(

    @Json(name = "deptlist")
    val deptlist: List<Deptlist> = listOf<Deptlist>()
)