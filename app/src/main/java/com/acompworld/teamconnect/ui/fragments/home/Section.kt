package com.acompworld.teamconnect.ui.fragments.home

import com.squareup.moshi.Json
import java.io.Serializable

enum class Section : Serializable {
    @Json(name="circular")
    CIRCULAR,
    @Json(name="news")
    NEWS,
    @Json(name="notice")
    NOTICE,
    @Json(name="event")
    EVENT,
    @Json(name="information")
    INFORMATION
}