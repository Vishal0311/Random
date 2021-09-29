package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Documenttab(
    @Json(name = "rid")
    val rid: Int?,
    @Json(name = "title_eng")
    val titleEng: String?,
    @Json(name = "title_hin")
    val titleHin: String?
):Serializable