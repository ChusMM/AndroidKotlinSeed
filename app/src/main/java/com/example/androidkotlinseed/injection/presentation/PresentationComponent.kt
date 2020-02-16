package com.example.androidkotlinseed.injection.presentation

import com.example.androidkotlinseed.view.activities.HeroDetailActivity
import com.example.androidkotlinseed.view.activities.HeroesListActivity
import com.example.androidkotlinseed.view.activities.PhotoViewerActivity
import com.example.androidkotlinseed.view.activities.SplashActivity
import dagger.Subcomponent

@Subcomponent(modules = [PresentationModule::class, ViewModelModule::class])
interface PresentationComponent {
    fun inject(splashActivity: SplashActivity)
    fun inject(heroesListActivity: HeroesListActivity)
    fun inject(heroDetailActivity: HeroDetailActivity)
    fun inject(photoViewerActivity: PhotoViewerActivity)
}