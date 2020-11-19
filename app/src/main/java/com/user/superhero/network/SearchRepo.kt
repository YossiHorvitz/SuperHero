package com.user.superhero.network

import androidx.lifecycle.MutableLiveData
import com.user.superhero.data.APIResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepo @Inject constructor(private val service: APIService) :
    CoroutineScope by MainScope() {

    val list = MutableLiveData<APIResponse>()
    val showProgress = MutableLiveData<Boolean>()

    fun searchHero(query: String) {
        showProgress.postValue(true)

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