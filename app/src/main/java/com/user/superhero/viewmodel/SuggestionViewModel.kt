package com.user.superhero.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.user.superhero.data.Hero
import com.user.superhero.network.SuggestionRepo

class SuggestionViewModel (application: Application) : AndroidViewModel(application) {

    private val repository = SuggestionRepo()
    val list: LiveData<Hero>

    init {
        this.list = repository.list
    }

    fun getHeroById(id: String) {
        repository.getHeroById(id)
    }
}