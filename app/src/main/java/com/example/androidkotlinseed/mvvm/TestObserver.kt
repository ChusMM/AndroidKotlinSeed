package com.example.androidkotlinseed.mvvm

import androidx.lifecycle.Observer

class TestObserver<T> : Observer<T> {
    private val observedValues = mutableListOf<T?>()

    override fun onChanged(value: T?) {
        observedValues.add(value)
    }
}