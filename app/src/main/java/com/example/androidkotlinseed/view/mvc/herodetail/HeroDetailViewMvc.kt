package com.example.androidkotlinseed.view.mvc.herodetail

import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.view.mvc.ViewMvc

interface HeroDetailViewMvc: ViewMvc {
    var viewListener: ViewListener?

    fun bindHero(hero: SuperHero?)

    interface ViewListener {
        fun onZoomClicked(hero: SuperHero)
    }
}