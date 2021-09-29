package com.acompworld.teamconnect.api.model.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

abstract class MasterResponse {
    @Json(name = "status")
    abstract val status: String

    @Json(name = "message")
    abstract val message: String
}
