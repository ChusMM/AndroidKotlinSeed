package com.example.androidkotlinseed.injection

import android.app.Application
import android.content.Context
import com.example.androidkotlinseed.domain.usecases.IFetchHeroesUseCase
import com.example.androidkotlinseed.injection.application.ApplicationModule
import com.example.androidkotlinseed.repository.DataStrategy
import com.example.androidkotlinseed.utils.AppRxSchedulers
import io.reactivex.schedulers.Schedulers
import org.powermock.api.mockito.PowerMockito.mock

class UnitTestApplicationModule(application: Application) : ApplicationModule(application) {
    override fun getFetchHeroesUserCase(dataStrategy: DataStrategy, context: Context): IFetchHeroesUseCase {
        return mock(IFetchHeroesUseCase::class.java)
    }

    override fun provideRxSchedulers() = AppRxSchedulers(
            database = Schedulers.trampoline(),
            disk = Schedulers.trampoline(),
            network = Schedulers.trampoline(),
            main = Schedulers.trampoline()
    )
}