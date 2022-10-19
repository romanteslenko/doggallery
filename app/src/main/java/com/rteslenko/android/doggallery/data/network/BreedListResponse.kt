package com.rteslenko.android.doggallery.data.network

import com.squareup.moshi.Json

data class BreedListResponse(
    @Json(name = "message") val message: Map<String, List<String>>,
    @Json(name = "status") val status: String
)
