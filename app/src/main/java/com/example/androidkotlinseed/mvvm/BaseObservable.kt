package com.example.androidkotlinseed.mvvm

import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class BaseObservable<L> {
    private val listeners: MutableSet<L> = Collections.newSetFromMap(
        ConcurrentHashMap<L, Boolean>(1))

    fun registerListener(listener: L) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: L) {
        listeners.remove(listener)
    }

    fun getListeners(): Set<L> {
        return Collections.unmodifiableSet(listeners)
    }
}