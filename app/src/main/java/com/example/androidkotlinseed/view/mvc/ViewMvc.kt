package com.example.androidkotlinseed.view.mvc

import android.view.View
import androidx.lifecycle.LifecycleObserver

interface ViewMvc : LifecycleObserver {
    var rootView: View
}