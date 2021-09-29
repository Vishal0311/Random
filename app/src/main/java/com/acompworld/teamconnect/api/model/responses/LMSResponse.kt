package com.acompworld.teamconnect.api.model.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LMSResponse(
    @Json(name = "status")
    val status: String?,
    @Json(name = "message")
    val message: String?,
    @Json(name = "data")
    val data: List<LMSResponseData>
)
@JsonClass(generateAdapter = true)
data class LMSResponseData (
    @Json(name = "category")
    val category: String,
    @Json(name = "resources")
    val resources: List<LMSResponseResource>
)
@JsonClass(generateAdapter = true)
data class LMSResponseResource (
    @Json(name = "resourceid")
    val resourceid: Int,
    @Json(name = "title")
    val title:String,
    @Json(name = "photopath")
    val photoPath: String,
    @Json(name = "attachmentURL")
    val attachmentURL: String,
    @Json(name = "filetype")
    val filetype: String,
    @Json(name = "isbookmarked")
    val isBookmarked:Boolean
)
