package com.example.androidkotlinseed.domain.usecases

import android.content.Context
import java.lang.ref.WeakReference

interface IBaseUseCase<L> {
    var listener: L?
    var contextRef: WeakReference<Context>?

    fun registerListener(listener: L) {
        this.listener = listener
    }

    fun unregisterListener(listener: L) {
        if (this.listener == listener) {
            this.listener = null
        }
    }

    fun setContextRef(context: Context) {
        this.contextRef = WeakReference(context)
    }

    fun getContextRef(): Context? {
        return contextRef?.get()
    }
}
