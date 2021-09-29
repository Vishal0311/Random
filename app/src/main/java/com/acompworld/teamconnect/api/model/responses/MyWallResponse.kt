package com.acompworld.teamconnect.api.model.responses


import com.acompworld.teamconnect.api.model.entities.Bannerlist
import com.acompworld.teamconnect.api.model.entities.FeedItem
import com.acompworld.teamconnect.api.model.entities.Generationdata
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyWallResponse(

    @Json(name = "projectcode")
    val projectcode: String,

    @Json(name = "projectname")
    val projectname: String,

    @Json(name = "bannerlist")
    val bannerlist: List<Bannerlist>,

    @Json(name = "generationdata")
    val generationData: List<Generationdata>?,

    @Json(name = "latesupdates")
    val latesupdates: List<FeedItem>?,

    @Json(name = "circulars")
    val circulars: List<FeedItem>?,

    @Json(name = "news")
    val news: List<FeedItem>?,

    @Json(name = "notices")
    val notices: List<FeedItem>?,

    @Json(name = "others")
    val others: List<FeedItem>?

)