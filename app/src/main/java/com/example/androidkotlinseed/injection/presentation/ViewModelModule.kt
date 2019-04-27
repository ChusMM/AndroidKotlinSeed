package com.example.androidkotlinseed.injection.presentation

import androidx.lifecycle.ViewModel
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import com.example.androidkotlinseed.mvvm.QuestionListViewModel
import com.example.androidkotlinseed.mvvm.ViewModelFactory
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import kotlin.reflect.KClass
import kotlin.annotation.Retention
import kotlin.annotation.Target

@Module
class ViewModelModule {
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @Retention(AnnotationRetention.RUNTIME)
    @MapKey
    annotation class ViewModelKey(val value: KClass<out ViewModel>)

    @Provides
    fun provideViewModelFactory(providers: Map<Class<out ViewModel>, Provider<ViewModel>>): ViewModelFactory {
        return ViewModelFactory(providers)
    }

    @Provides
    @IntoMap
    @ViewModelKey(QuestionListViewModel::class)
    fun getHeroListViewModel(fetchHeroesUseCase: FetchHeroesUseCase): QuestionListViewModel {
        return QuestionListViewModel(fetchHeroesUseCase)
    }
}