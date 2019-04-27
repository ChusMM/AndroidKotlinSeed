package com.example.androidkotlinseed.repository

import com.example.androidkotlinseed.SuperHero
import com.example.androidkotlinseed.api.HeroListWrapper
import com.example.androidkotlinseed.api.HeroWrapper

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
        return SuperHero(0, name, photo, realName, height, power, abilities, groups)
    }
}