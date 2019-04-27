package com.example.androidkotlinseed.injection.application

import dagger.Component

@Component(modules = arrayOf(ApplicationModule::class, UseCaseModule::class))
interface ApplicationComponent {
}