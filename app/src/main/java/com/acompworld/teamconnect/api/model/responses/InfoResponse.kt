package com.acompworld.teamconnect.api.model.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InfoResponse(
    @Json(name = "attachmentpath1")
    val attachmentPath1: String?,
    @Json(name = "attachmentpath2")
    val attachmentPath2: String?,
    @Json(name = "attachmentpath3")
    val attachmentPath3: String?,
    @Json(name = "attachmentpath4")
    val attachmentPath4: String?,
    @Json(name = "attachmentpath5")
    val attachmentPath5: String?,
    @Json(name = "attachmenttitle1")
    val attachmentTitle1: String?,
    @Json(name = "attachmenttitle2")
    val attachmentTitle2: String?,
    @Json(name = "attachmenttitle3")
    val attachmentTitle3: String?,
    @Json(name = "attachmenttitle4")
    val attachmentTitle4: String?,
    @Json(name = "attachmenttitle5")
    val attachmentTitle5: String?,
    @Json(name = "linkurl")
    val linkurl: String?,
    @Json(name = "postedby")
    val postedby: String?,
    @Json(name = "postedon")
    val postedon: String?,
    @Json(name = "rid")
    val rid: Int?,
    @Json(name = "titleeng")
    val titleEng: String?,
    @Json(name = "titlehin")
    val titleHin: String?,
    @Json(name = "type")
    val type: String?
)