package com.rteslenko.android.doggallery.domain

import com.rteslenko.android.doggallery.data.model.Breed
import com.rteslenko.android.doggallery.data.network.DogCeoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBreedListUseCase {

    sealed class Result {
        data class Success(val breedList: List<Breed>) : Result()
        object Failure : Result()
    }

    suspend fun fetch(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val response = DogCeoApi.retrofitService.getBreedListResponse()
                if (response.status != "success") {
                    Result.Failure
                } else {
                    val breedList = response.message.map { entry ->
                        Breed(entry.key)
                    }
                    Result.Success(breedList)
                }
            } catch (e: Exception) {
                Result.Failure
            }
        }
    }
}