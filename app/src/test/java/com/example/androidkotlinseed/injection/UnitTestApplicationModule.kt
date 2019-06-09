package com.example.androidkotlinseed.injection

import android.app.Application
import android.content.Context
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import com.example.androidkotlinseed.injection.application.ApplicationModule
import com.example.androidkotlinseed.repository.DataStrategy
import org.powermock.api.mockito.PowerMockito.spy

class UnitTestApplicationModule(application: Application) : ApplicationModule(application) {
    override fun getFetchHeroesUserCase(dataStrategy: DataStrategy, context: Context): FetchHeroesUseCase {
        return spy(super.getFetchHeroesUserCase(dataStrategy, context))
    }
}