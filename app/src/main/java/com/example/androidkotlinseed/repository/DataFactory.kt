package com.example.androidkotlinseed.repository

import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.api.HeroListWrapper
import com.example.androidkotlinseed.api.HeroWrapper
import com.example.androidkotlinseed.domain.SuperHero
import retrofit2.HttpException

class DataFactory {
    fun superHeroesFromHeroListWrapper(heroListWrapper: HeroListWrapper): List<SuperHero> {
        val superHeroes: MutableList<SuperHero> = arrayListOf()

        for (heroWrapper in heroListWrapper.superheroes) {
            val hero = superHeroFromHeroWrapper(heroWrapper)
            superHeroes.add(hero)
        }
        return superHeroes
    }

    private fun superHeroFromHeroWrapper(heroWrapper: HeroWrapper): SuperHero {
        val (name, photo, realName, height, power, abilities, groups) = heroWrapper
        return SuperHero(
                name,
                photo,
                realName,
                height,
                power,
                abilities,
                groups
        )
    }

    fun callErrorFromThrowable(error: Throwable): CallError {
        return (error as? HttpException)?.let {
            CallError.buildFromErrorCode(it.code())
        } ?: CallError.UNKNOWN_ERROR
    }
}