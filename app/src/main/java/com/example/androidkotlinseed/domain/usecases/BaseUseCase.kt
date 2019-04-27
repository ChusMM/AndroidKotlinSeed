package com.example.androidkotlinseed.domain.usecases

import android.content.Context
import com.example.androidkotlinseed.mvvm.BaseObservable
import java.lang.ref.WeakReference

abstract class BaseUseCase<L> : BaseObservable<L>() {

    private var contextRef: WeakReference<Context>? = null

    fun setContextRef(context: Context) {
        this.contextRef = WeakReference(context)
    }

    fun getContextRef(): Context? {
        return contextRef?.get()
    }
}