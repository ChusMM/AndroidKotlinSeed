package com.example.androidkotlinseed.mvvm

import android.view.View
import androidx.lifecycle.LifecycleObserver

interface ViewMvc: LifecycleObserver {
    var rootView: View
}