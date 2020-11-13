package com.user.superhero.network

import com.user.superhero.data.APIResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    companion object {
        private const val ACCESS_KEY = "3881543648526468"
        const val BASE_URL = "https://superheroapi.com/api/$ACCESS_KEY/"
    }

    @GET("search/{query}")
    suspend fun searchHero(@Path("query") query: String): Response<APIResponse>

    @GET("{id}")
    fun getHeroById(@Path("id") id: String): Call<APIResponse.Results>
}