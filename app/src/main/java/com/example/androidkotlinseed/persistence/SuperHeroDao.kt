package com.example.androidkotlinseed.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidkotlinseed.domain.SuperHero
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface SuperHeroDao {
    @Query("SELECT * FROM super_heroes")
    fun getAll(): Observable<List<SuperHero>>

    @Query("SELECT * FROM super_heroes WHERE uid IN (:heroIds)")
    fun loadAllByIds(heroIds: IntArray): List<SuperHero>

    @Query("SELECT * FROM super_heroes WHERE name LIKE :first LIMIT 1")
    fun findByName(first: String): SuperHero

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(superHeroes: List<SuperHero>): Completable

    @Query("DELETE FROM super_heroes")
    fun deleteAll(): Single<Int>
}