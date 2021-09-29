package com.acompworld.teamconnect.api.model.responses


import com.acompworld.teamconnect.api.model.entities.Image
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AboutRespone(
    @Json(name = "aboutus_eng")
    val aboutusEng: String="",
    @Json(name = "aboutus_hin")
    val aboutusHin: String="",
    @Json(name = "address_eng")
    val addressEng: String="",
    @Json(name = "address_hin")
    val addressHin: String="",
    @Json(name = "corevalues_eng")
    val corevaluesEng: String="",
    @Json(name = "corevalues_hin")
    val corevaluesHin: String="",
    @Json(name = "googlemap")
    val googlemap: String?,
    @Json(name = "imagepath")
    val imagepath: String="",
    @Json(name = "images")
    val images: List<Image>,
    @Json(name = "mission_eng")
    val missionEng: String="",
    @Json(name = "mission_hin")
    val missionHin: String="",
    @Json(name = "projectcode")
    val projectcode: String,
    @Json(name = "projectname")
    val projectname: String,
    @Json(name = "reachviaflight_eng")
    val reachviaflightEng: String="",
    @Json(name = "reachviaflight_hin")
    val reachviaflightHin: String="",
    @Json(name = "reachviaroad_eng")
    val reachviaroadEng: String="",
    @Json(name = "reachviaroad_hin")
    val reachviaroadHin: String="",
    @Json(name = "reachviatrain_eng")
    val reachviatrainEng: String="",
    @Json(name = "reachviatrain_hin")
    val reachviatrainHin: String="",
    @Json(name = "vision_eng")
    val visionEng: String="",
    @Json(name = "vision_hin")
    val visionHin: String=""
)