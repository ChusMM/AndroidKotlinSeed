package com.example.androidkotlinseed.mvvm

import androidx.lifecycle.Observer
import com.example.androidkotlinseed.domain.SuperHero

interface HeroListViewMvc: ViewMvc {
    fun getViewModelObserver(): Observer<List<SuperHero>>
    fun onHeroesFetched(superHeroes: List<SuperHero>)
    fun onHeroesFetchFailed(msg: String)
    fun onClickHero(superHero: SuperHero)
}