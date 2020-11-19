package com.user.superhero.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.user.superhero.data.APIResponse
import com.user.superhero.network.SearchRepo

class SearchViewModel @ViewModelInject constructor(
    private val repository: SearchRepo
) : ViewModel() {

    val list: LiveData<APIResponse>
    val showProgress: LiveData<Boolean>

    init {
        this.list = repository.list
        this.showProgress = repository.showProgress
    }

    fun searchHero(query: String) {
        repository.searchHero(query)
    }
}