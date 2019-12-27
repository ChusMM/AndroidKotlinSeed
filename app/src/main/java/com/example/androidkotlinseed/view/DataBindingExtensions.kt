package com.example.androidkotlinseed.view

import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

@Suppress("unused")
inline fun <reified T : ViewDataBinding> Fragment.inflateBinding(@LayoutRes layoutId: Int): T {
    return DataBindingUtil.inflate(LayoutInflater.from(requireContext()), layoutId, null, false)
}
