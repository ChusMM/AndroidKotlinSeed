package com.example.androidkotlinseed.injection

import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.example.androidkotlinseed.App
import com.example.androidkotlinseed.injection.application.ApplicationComponent
import com.example.androidkotlinseed.injection.presentation.PresentationComponent
import com.example.androidkotlinseed.injection.presentation.PresentationModule
import com.example.androidkotlinseed.injection.presentation.ViewModelModule

abstract class BaseActivity : AppCompatActivity() {
    var isInjectorUsed = false

    @UiThread
    fun getPresentationComponent(): PresentationComponent {
        if (isInjectorUsed) {
            throw RuntimeException("There is no need to use injector more than once")
        }
        isInjectorUsed = true
        return getApplicationComponent()
            .newPresentationComponent(PresentationModule(this))
    }

    private fun getApplicationComponent(): ApplicationComponent {
        return (application as App).getApplicationComponent()
    }
}