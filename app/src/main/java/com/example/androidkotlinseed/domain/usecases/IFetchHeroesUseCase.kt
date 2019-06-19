package com.example.androidkotlinseed.domain.usecases

import com.example.androidkotlinseed.domain.SuperHero

interface IFetchHeroesUseCase: IBaseUseCase<IFetchHeroesUseCase.Listener> {
    fun fetchAndNotify()
    fun dispose()

    interface Listener {
        fun onFetchHeroesOk(superHeroes: List<SuperHero>)
        fun onFetchHeroesFailed(msg: String)
    }
}