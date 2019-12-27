package com.example.androidkotlinseed.injection

import com.example.androidkotlinseed.injection.application.ApplicationComponent
import com.example.androidkotlinseed.injection.application.ApplicationModule
import com.example.androidkotlinseed.injection.application.UseCaseModule
import com.example.androidkotlinseed.mvvm.HeroViewModelUnitTest
import com.example.androidkotlinseed.repository.DataWebServiceUnitTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, UseCaseModule::class])
interface UnitTestApplicationComponent : ApplicationComponent {
    fun inject(heroViewModelUnitTest: HeroViewModelUnitTest)
    fun inject(dataWebServiceUnitTest: DataWebServiceUnitTest)
}