package com.acompworld.teamconnect.api.model.responses


import com.acompworld.teamconnect.api.model.entities.Documenttab
import com.acompworld.teamconnect.api.model.entities.Update
import com.acompworld.teamconnect.api.model.entities.Bannerlist
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DepartmentInfoResponse(
    @Json(name = "aboutdept_eng")
    val aboutdeptEng: String?,
    @Json(name = "aboutdept_hin")
    val aboutdeptHin: String?,
    @Json(name = "bannerlist")
    val bannerlist: List<Bannerlist>?,
    @Json(name = "department")
    val department: String?,
    @Json(name = "department_hin")
    val departmentHin: String?,
    @Json(name = "deptid")
    val deptid: Int,
    @Json(name = "documenttabs")
    val documenttabs: List<Documenttab>?,
    @Json(name = "mission_eng")
    val missionEng: String?,
    @Json(name = "mission_hin")
    val missionHin: String?,
    @Json(name = "updates")
    val updates: List<Update>?,
    @Json(name = "vision_eng")
    val visionEng: String?,
    @Json(name = "vision_hin")
    val visionHin: String?
)