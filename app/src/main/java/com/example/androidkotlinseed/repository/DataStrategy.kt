package com.example.androidkotlinseed.repository

import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero

interface DataStrategy {

    interface QueryHeroesListener {
        fun onQueryHeroesOk(superHeroes: List<SuperHero>)
        fun onQueryHeroesFailed(callError: CallError)
    }

    fun queryHeroes(queryHeroesListener: QueryHeroesListener)
}