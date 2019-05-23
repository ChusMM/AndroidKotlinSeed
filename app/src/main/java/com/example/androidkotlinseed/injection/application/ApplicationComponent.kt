package com.example.androidkotlinseed.injection.application

import com.example.androidkotlinseed.injection.presentation.BindingComponent
import com.example.androidkotlinseed.injection.presentation.BindingsModule
import com.example.androidkotlinseed.injection.presentation.PresentationComponent
import com.example.androidkotlinseed.injection.presentation.PresentationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, UseCaseModule::class])
interface ApplicationComponent {
    fun newPresentationComponent(presentationModule: PresentationModule): PresentationComponent
    fun newBindingComponent(bindingsModule: BindingsModule): BindingComponent
}