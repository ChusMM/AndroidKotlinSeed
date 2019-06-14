package com.example.androidkotlinseed.injection.presentation

import com.example.androidkotlinseed.view.activities.HeroDetailActivity
import com.example.androidkotlinseed.view.activities.HeroesListActivity
import dagger.Subcomponent

@Subcomponent(modules = [PresentationModule::class, ViewModelModule::class])
interface PresentationComponent {
    fun inject(heroesListActivity: HeroesListActivity)
    fun inject(heroDetailActivity: HeroDetailActivity)
}