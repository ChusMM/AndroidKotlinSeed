package com.example.androidkotlinseed.injection.application

import android.app.Application
import android.content.Context
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import com.example.androidkotlinseed.domain.usecases.IFetchHeroesUseCase
import com.example.androidkotlinseed.repository.DataStrategy
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class ApplicationModule(private val application: Application) {
    
    @Singleton
    @Provides
    fun getApplication(): Application {
        return application
    }

    @Provides
    fun getAppContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    open fun getFetchHeroesUserCase(dataStrategy: DataStrategy, context: Context)
            : IFetchHeroesUseCase {
        return FetchHeroesUseCase(dataStrategy, context)
    }
}
