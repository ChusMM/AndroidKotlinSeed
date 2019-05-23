package com.example.androidkotlinseed.injection.application

import android.app.Application
import android.content.Context
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import com.example.androidkotlinseed.repository.DataStrategy
import com.example.androidkotlinseed.view.adapters.HeroBindingAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {
    
    @Singleton
    @Provides
    fun getApplication(): Application {
        return application
    }

    @Provides
    fun getAppContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun getHeroBindingAdapter(): HeroBindingAdapter = HeroBindingAdapter(application)

    @Provides
    fun getFetchHeroesUserCase(dataStrategy: DataStrategy, context: Context)
            : FetchHeroesUseCase {
        return FetchHeroesUseCase(dataStrategy, context)
    }
}