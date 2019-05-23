package com.example.androidkotlinseed.injection

import android.app.Application
import androidx.annotation.UiThread
import com.example.androidkotlinseed.App
import com.example.androidkotlinseed.injection.application.ApplicationComponent
import com.example.androidkotlinseed.injection.presentation.BindingComponent
import com.example.androidkotlinseed.injection.presentation.BindingsModule

open class BaseBindingAdapter(val application: Application) {
    var isInjectorUsed = false

    @UiThread
    fun getBindingComponent(): BindingComponent {
        if (isInjectorUsed) {
            throw RuntimeException("There is no need to use injector more than once")
        }
        isInjectorUsed = true
        return getApplicationComponent().newBindingComponent(BindingsModule())
    }

    private fun getApplicationComponent(): ApplicationComponent {
        return (application as App).getApplicationComponent()
    }
}