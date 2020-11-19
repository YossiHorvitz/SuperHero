package com.user.superhero.network

import com.user.superhero.BuildConfig
import com.user.superhero.data.APIResponse
import com.user.superhero.data.Hero
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    companion object {
        const val BASE_URL = "https://superheroapi.com/api/${BuildConfig.API_KEY}/"
    }

    @GET("search/{query}")
    suspend fun searchHero(@Path("query") query: String): Response<APIResponse>

    @GET("{id}")
    suspend fun getHeroById(@Path("id") id: String): Response<Hero>
}