package com.example.androidkotlinseed.api

import io.reactivex.Observable
import retrofit2.http.GET

interface MarvelApi {
    @GET("/bins/bvyob")
    fun getHeroes(): Observable<HeroListWrapper>
}