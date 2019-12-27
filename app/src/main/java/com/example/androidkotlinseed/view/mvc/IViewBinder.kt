package com.example.androidkotlinseed.view.mvc

interface IViewBinder<V> {
    val viewBinders: MutableSet<V>

    fun registerViewBinder(listener: V) {
        viewBinders.add(listener)
    }

    fun unregisterViewBinder(listener: V) {
        viewBinders.remove(listener)
    }
}