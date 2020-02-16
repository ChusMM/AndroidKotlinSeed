package com.example.androidkotlinseed.injection.application

import com.example.androidkotlinseed.MockApp
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, UseCaseModule::class])
interface MockApplicationComponent : ApplicationComponent {
    fun inject(app: MockApp)
}