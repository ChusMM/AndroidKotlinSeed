package com.example.androidkotlinseed.api

import io.reactivex.Observable
import retrofit2.http.GET

interface MarvelApi {
    companion object {
        const val GET_HEROES_PATH = "/bins/bvyob"
    }

    @GET(GET_HEROES_PATH)
    fun getHeroes(): Observable<HeroListWrapper>
}