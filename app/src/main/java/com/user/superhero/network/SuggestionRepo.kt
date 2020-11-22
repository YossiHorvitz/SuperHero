package com.user.superhero.network

import androidx.lifecycle.MutableLiveData
import com.user.superhero.data.Hero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuggestionRepo @Inject constructor(private val service: APIService) :
    CoroutineScope by MainScope() {

    val list = MutableLiveData<Hero>()

    fun getHeroById(id: String) {

        launch(Dispatchers.IO) {
            try {
                val response = service.getHeroById(id)
                if (response.isSuccessful && response.body() != null) {
                    list.postValue(response.body())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}