package com.example.androidkotlinseed.view.mvc.herodetail

import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.view.mvc.ViewMvc

interface HeroDetailViewMvc: ViewMvc {
    fun bindHero(hero: SuperHero?)
}