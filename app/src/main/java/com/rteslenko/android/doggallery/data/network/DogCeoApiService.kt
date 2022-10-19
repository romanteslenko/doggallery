package com.rteslenko.android.doggallery.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://dog.ceo/api/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface DogCeoApiService {
    @GET("breeds/list/all")
    suspend fun getBreedListResponse(): BreedListResponse

    @GET("breed/{breed}/images")
    suspend fun getImageUrls(@Path("breed") breedName: String): BreedImagesResponse
}

object DogCeoApi {
    val retrofitService: DogCeoApiService by lazy {
        retrofit.create(DogCeoApiService::class.java)
    }
}