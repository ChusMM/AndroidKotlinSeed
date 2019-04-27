package com.example.androidkotlinseed.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidkotlinseed.SuperHero

@Database(entities = arrayOf(SuperHero::class), version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun superHeroDao(): SuperHeroDao
}