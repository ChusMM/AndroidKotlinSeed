package com.example.androidkotlinseed.mvvm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.view.mvc.IViewBinder
import com.example.androidkotlinseed.view.mvc.herodetail.HeroDetailViewMvc

class HeroDetailViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(), IViewBinder<HeroDetailViewMvc> {

    val heroBound: LiveData<SuperHero> get() = _heroBound
    private val _heroBound: MutableLiveData<SuperHero> by lazy {
        MutableLiveData<SuperHero>()
    }

    override val viewBinders: MutableSet<HeroDetailViewMvc> = mutableSetOf()

    fun bindHero(hero: SuperHero?) {
        _heroBound.value = hero
    }
}