package com.user.superhero.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.user.superhero.data.Hero
import com.user.superhero.network.SuggestionRepo

class SuggestionViewModel @ViewModelInject constructor(private val repository: SuggestionRepo) : ViewModel() {

    val list: LiveData<Hero>

    init {
        this.list = repository.list
    }

    fun getHeroById(id: String) {
        repository.getHeroById(id)
    }
}