package com.acompworld.teamconnect.api.model.responses


import com.acompworld.teamconnect.api.model.entities.Empdirectory
import com.acompworld.teamconnect.api.model.entities.Teldirectory
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DirectoryResponse(
    @Json(name = "empdirectory")
    val empdirectory: List<Empdirectory>?,
    @Json(name = "ProjectCode")
    val projectCode: String,
    @Json(name = "ProjectName")
    val projectName: String,
    @Json(name = "teldirectory")
    val teldirectory: List<Teldirectory>?
)