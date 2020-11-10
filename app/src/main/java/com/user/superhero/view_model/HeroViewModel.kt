package com.user.superhero.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.user.superhero.SuperHeroApplication
import com.user.superhero.api.APIResponse
import java.util.*

class HeroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SuperHeroApplication()
    val showProgress: LiveData<Boolean>
    val list: LiveData<APIResponse>

    init {
        this.list = repository.list
        this.showProgress = repository.showProgress
    }

    fun searchHero(query: String) {
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