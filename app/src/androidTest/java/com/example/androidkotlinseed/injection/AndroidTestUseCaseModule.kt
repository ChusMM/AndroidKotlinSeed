package com.example.androidkotlinseed.injection

import com.example.androidkotlinseed.injection.application.UseCaseModule
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AndroidTestUseCaseModule : UseCaseModule() {
    companion object {
        private const val BASE_URL = "http://localhost:8080/"
    }

    override fun getRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }
}