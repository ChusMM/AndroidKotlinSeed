package com.example.androidkotlinseed.injection.presentation

import com.example.androidkotlinseed.view.activities.HeroDetailActivity
import com.example.androidkotlinseed.view.activities.HeroesListActivityMvc
import dagger.Subcomponent

@Subcomponent(modules = [PresentationModule::class, ViewModelModule::class])
interface PresentationComponent {
    fun inject(heroesListActivity: HeroesListActivityMvc)
    fun inject(heroDetailActivity: HeroDetailActivity)
}