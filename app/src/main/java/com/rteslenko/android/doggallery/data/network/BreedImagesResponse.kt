package com.rteslenko.android.doggallery.data.network

import com.squareup.moshi.Json

data class BreedImagesResponse(
    @Json(name = "message") val urls: List<String>,
    @Json(name = "status") val status: String
)
