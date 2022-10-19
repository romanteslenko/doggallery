package com.rteslenko.android.doggallery.domain

import com.rteslenko.android.doggallery.data.model.Breed
import com.rteslenko.android.doggallery.data.network.DogCeoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBreedImageUrlsUseCase(private val breed: Breed) {

    sealed interface Response {
        object Failure : Response
        data class Success(val urls: List<String>) : Response
    }

    suspend fun fetch(): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response = DogCeoApi.retrofitService.getImageUrls(breed.name)
                if (response.status != "success") {
                    Response.Failure
                } else {
                    Response.Success(response.urls)
                }
            } catch (e: Exception) {
                Response.Failure

            }
        }
    }
}
