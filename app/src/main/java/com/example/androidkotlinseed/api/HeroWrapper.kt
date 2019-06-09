package com.example.androidkotlinseed.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class HeroWrapper(@SerializedName("name") var name: String = EMPTY_NAME,
                       @SerializedName("photo") val photo: String?,
                       @SerializedName("realName") val realName: String?,
                       @SerializedName("height") val height: String?,
                       @SerializedName("power") val power: String?,
                       @SerializedName("abilities") val abilities: String?,
                       @SerializedName("groups") val groups: String?) {
    companion object {
        private const val EMPTY_NAME = "no_name"

        fun fromJson(json: String): HeroWrapper {
            val gson = Gson()
            return gson.fromJson(json, HeroWrapper::class.java)
        }
    }
}