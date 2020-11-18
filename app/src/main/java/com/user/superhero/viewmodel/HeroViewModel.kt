package com.user.superhero.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.user.superhero.data.APIResponse
import com.user.superhero.network.Repository

class HeroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository()
    val showProgress: LiveData<Boolean>
    val list: LiveData<APIResponse>

    init {
        this.list = repository.list
        this.showProgress = repository.showProgress
    }

    fun searchHero(query: String) {
        repository.searchHero(query)
    }
}