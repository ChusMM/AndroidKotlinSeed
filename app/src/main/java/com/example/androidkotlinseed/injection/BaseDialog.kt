package com.example.androidkotlinseed.injection

import androidx.annotation.UiThread
import androidx.fragment.app.DialogFragment
import com.example.androidkotlinseed.App
import com.example.androidkotlinseed.injection.application.ApplicationComponent
import com.example.androidkotlinseed.injection.presentation.PresentationComponent
import com.example.androidkotlinseed.injection.presentation.PresentationModule
import com.example.androidkotlinseed.injection.presentation.ViewModelModule

class BaseDialog: DialogFragment() {
    var isInjectorUsed = false

    @UiThread
    fun getPresentationComponent(): PresentationComponent {
        if (isInjectorUsed) {
            throw RuntimeException("There is no need to use injector more than once")
        }
        isInjectorUsed = true
        return getApplicationComponent().newPresentationComponent(
            PresentationModule(activity ?: throw java.lang.RuntimeException("No activity attached")))
    }

    private fun getApplicationComponent(): ApplicationComponent {
        return (activity?.application as App).getApplicationComponent()
    }
}