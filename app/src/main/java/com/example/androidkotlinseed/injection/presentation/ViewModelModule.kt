package com.example.androidkotlinseed.injection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidkotlinseed.mvvm.HeroDetailViewModel
import com.example.androidkotlinseed.mvvm.HeroListViewModel
import com.example.androidkotlinseed.mvvm.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap

import kotlin.reflect.KClass
import kotlin.annotation.Retention

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HeroListViewModel::class)
    abstract fun heroListViewModel(heroListViewModel: HeroListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HeroDetailViewModel::class)
    abstract fun heroDetailViewModel(heroDetailViewModel: HeroDetailViewModel): ViewModel
}
