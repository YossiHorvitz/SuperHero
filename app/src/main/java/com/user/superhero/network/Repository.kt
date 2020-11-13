package com.user.superhero.network

import androidx.lifecycle.MutableLiveData
import com.user.superhero.data.APIResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository : CoroutineScope by MainScope() {

    val list = MutableLiveData<APIResponse>()
    val showProgress = MutableLiveData<Boolean>()

    fun searchHero(query: String) {
        showProgress.postValue(true)

        val retrofit = Retrofit.Builder()
            .baseUrl(APIService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)


            launch(Dispatchers.IO) {
                try {
                    val response = service.searchHero(query)
                    if (response.isSuccessful && response.body() != null) {
                        list.postValue(response.body())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                showProgress.postValue(false)
            }

    }
}