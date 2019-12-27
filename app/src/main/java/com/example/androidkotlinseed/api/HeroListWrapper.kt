package com.example.androidkotlinseed.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class HeroListWrapper(@SerializedName("superheroes")
                      var superheroes: List<HeroWrapper> = arrayListOf()) {
    companion object {
        fun fromJson(json: String): HeroListWrapper {
            val gson = Gson()
            return gson.fromJson(json, HeroListWrapper::class.java)
        }
    }
}