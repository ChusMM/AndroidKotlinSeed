package com.example.androidkotlinseed.domain.usecases

import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.repository.DataStrategy

interface IFetchHeroesUseCase: IBaseUseCase<IFetchHeroesUseCase.Listener>, DataStrategy.QueryHeroesListener {
    fun fetchAndNotify()
    fun dispose()

    interface Listener {
        fun onFetchHeroesOk(superHeroes: List<SuperHero>)
        fun onFetchHeroesFailed(msg: String)
    }
}