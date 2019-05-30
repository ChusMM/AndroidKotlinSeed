package com.example.androidkotlinseed.domain.usecases

import android.content.Context
import java.lang.ref.WeakReference

abstract class BaseUseCase<L> {
    private var listener: L? = null

    private var contextRef: WeakReference<Context>? = null

    fun registerListener(listener: L) {
        this.listener = listener
    }

    fun unregisterListener(listener: L) {
        if (this.listener == listener) {
            this.listener = null
        }
    }

    protected fun getListener(): L? = listener

    fun setContextRef(context: Context) {
        this.contextRef = WeakReference(context)
    }

    fun getContextRef(): Context? {
        return contextRef?.get()
    }
}