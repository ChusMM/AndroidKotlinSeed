package com.example.androidkotlinseed.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.view.mvc.IViewBinder
import com.example.androidkotlinseed.view.mvc.herodetail.HeroDetailViewMvc
import javax.inject.Inject

class HeroDetailViewModel @Inject constructor() : ViewModel(), IViewBinder<HeroDetailViewMvc> {

    val heroBound: MutableLiveData<SuperHero> by lazy {
        @Suppress("RemoveExplicitTypeArguments")
        MutableLiveData<SuperHero>()
    }

    override val viewBinders: MutableSet<HeroDetailViewMvc> = mutableSetOf()

    fun bindHero(hero: SuperHero?) {
        heroBound.value = hero
    }
}