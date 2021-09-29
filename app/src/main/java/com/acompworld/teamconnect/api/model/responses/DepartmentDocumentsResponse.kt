package com.acompworld.teamconnect.api.model.responses


import com.acompworld.teamconnect.api.model.entities.Document
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DepartmentDocumentsResponse(
    @Json(name = "doclevel")
    val doclevel: Int?,
    @Json(name = "parentdocrid")
    val parentdocrid: Int?,
    @Json(name = "rid")
    val rid: Int?,
    @Json(name = "title_eng")
    val titleEng: String?,
    @Json(name = "title_hin")
    val titleHin: String?,
    @Json(name = "documents")
    val documents: List<Document>?
)