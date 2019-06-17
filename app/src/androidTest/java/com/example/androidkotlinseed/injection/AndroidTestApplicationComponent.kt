package com.example.androidkotlinseed.injection

import com.example.androidkotlinseed.HeroesListActivityTest
import com.example.androidkotlinseed.injection.application.ApplicationComponent
import com.example.androidkotlinseed.injection.application.ApplicationModule
import com.example.androidkotlinseed.injection.application.UseCaseModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, UseCaseModule::class])
interface AndroidTestApplicationComponent : ApplicationComponent {
    fun inject(heroesListActivityTest: HeroesListActivityTest)
}