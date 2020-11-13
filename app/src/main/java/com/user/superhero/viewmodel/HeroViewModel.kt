package com.user.superhero.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.user.superhero.data.APIResponse
import com.user.superhero.network.Repository
import java.util.*

class HeroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository()
    val showProgress: LiveData<Boolean>
    val list: LiveData<APIResponse>

    init {
        this.list = repository.list
        this.showProgress = repository.showProgress
    }

    suspend fun searchHero(query: String) {
        repository.searchHero(query)
    }

    fun getRandomHero() {
        for (x in 0..2) {
            getRandomHero(Random().nextInt(731))
        }
    }

    fun getRandomHero(id: Int) {
    }
}