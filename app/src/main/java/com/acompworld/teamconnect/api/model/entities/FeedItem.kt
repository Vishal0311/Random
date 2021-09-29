package com.acompworld.teamconnect.api.model.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedItem(
    @Json(name = "attachmentpath")
    val attachmentpath: String?,
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
    val type: String?,
    @Json(name="totalattachments")
    val totalAttachments : String = ""

)