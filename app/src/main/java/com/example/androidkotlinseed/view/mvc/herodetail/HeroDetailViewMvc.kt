package com.example.androidkotlinseed.view.mvc.herodetail

import androidx.lifecycle.Observer
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.view.mvc.ViewMvc

interface HeroDetailViewMvc: ViewMvc {
    var viewListener: ViewListener?
    val heroDetailObserver: Observer<SuperHero>

    interface ViewListener {
        fun onZoomClicked(hero: SuperHero)
    }
}