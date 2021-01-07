package com.example.androidkotlinseed.injection.application

import android.app.Application
import android.content.Context
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import com.example.androidkotlinseed.domain.usecases.IFetchHeroesUseCase
import com.example.androidkotlinseed.repository.DataStrategy
import com.example.androidkotlinseed.utils.AppRxSchedulers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
open class ApplicationModule {

    @Provides
    fun getAppContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    open fun getFetchHeroesUserCase(dataStrategy: DataStrategy, context: Context)
            : IFetchHeroesUseCase {
        return FetchHeroesUseCase(dataStrategy, context)
    }

    @Singleton
    @Provides
    open fun provideRxSchedulers() = AppRxSchedulers(
            database = Schedulers.single(),
            disk = Schedulers.io(),
            network = Schedulers.io(),
            main = AndroidSchedulers.mainThread()
    )

    @Provides
    fun provideMockWebServer() = MockWebServer()
}
