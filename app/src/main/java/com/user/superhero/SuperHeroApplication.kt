package com.user.superhero

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.user.superhero.api.APIResponse
import com.user.superhero.api.APIService
import com.user.superhero.api.APIService.Companion.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SuperHeroApplication : Application() {

    val list = MutableLiveData<APIResponse>()
    val showProgress = MutableLiveData<Boolean>()

    fun searchHero(query: String) {
        showProgress.value = true

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)

        try {
            service.searchHero(query).enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    showProgress.value = false
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                    list.value = response.body()
                    showProgress.value = false
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}