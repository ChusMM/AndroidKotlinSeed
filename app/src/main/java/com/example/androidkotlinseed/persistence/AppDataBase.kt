package com.example.androidkotlinseed.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidkotlinseed.domain.ExpirationTable
import com.example.androidkotlinseed.domain.SuperHero

@Database(entities = [SuperHero::class, ExpirationTable::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun superHeroDao(): SuperHeroDao
}