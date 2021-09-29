package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Document(
    @Json(name = "attachmentpath")
    val attachmentpath: String?,
    @Json(name = "doclevel")
    val doclevel: Int?,
    @Json(name = "extension")
    val extension: String?,
    @Json(name = "isdirectory")
    val isdirectory: Boolean?,
    @Json(name = "linkurl")
    val linkurl: Any?,
    @Json(name = "parentdocrid")
    val parentdocrid: String?,
    @Json(name = "rid")
    val rid: String?,
    @Json(name = "title_eng")
    val titleEng: String?,
    @Json(name = "title_hin")
    val titleHin: String?
)