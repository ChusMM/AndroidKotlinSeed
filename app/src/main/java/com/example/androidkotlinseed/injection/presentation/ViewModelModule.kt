package com.example.androidkotlinseed.injection.presentation

import androidx.lifecycle.ViewModel
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import com.example.androidkotlinseed.mvvm.HeroListViewModel
import com.example.androidkotlinseed.mvvm.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

import javax.inject.Provider
import kotlin.reflect.KClass
import kotlin.annotation.Target
import kotlin.annotation.Retention

@Module
class ViewModelModule {
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @Retention(AnnotationRetention.RUNTIME)
    @MapKey
    internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

    @Binds
    internal fun viewModelFactory(providerMap: Map<Class<out ViewModel>, Provider<ViewModel>>): ViewModelFactory {
        return ViewModelFactory(providerMap)
    }

    @Binds
    @IntoMap
    @ViewModelKey(HeroListViewModel::class)
    internal fun questionDetailsViewModel(fetchHeroesUseCase: FetchHeroesUseCase): ViewModel {
        return HeroListViewModel(fetchHeroesUseCase)
    }
}
