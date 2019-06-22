package com.example.androidkotlinseed.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidkotlinseed.domain.ExpirationTable
import com.example.androidkotlinseed.domain.SuperHero
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface SuperHeroDao {
    @Query("SELECT * FROM super_heroes")
    fun getAll(): Observable<List<SuperHero>>

    @Query("SELECT * FROM super_heroes WHERE uid IN (:heroIds)")
    fun loadAllByIds(heroIds: IntArray): Observable<List<SuperHero>>

    @Query("SELECT * FROM super_heroes WHERE name LIKE :first LIMIT 1")
    fun findByName(first: String): SuperHero

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(superHeroes: List<SuperHero>): Completable

    @Query("DELETE FROM super_heroes")
    fun deleteAll(): Single<Int>

    @Query("SELECT * FROM expiration_table WHERE entity = :entity")
    fun getHeroesExpirationTable(entity: String = ExpirationEntity.HEROES.entityName): Single<ExpirationTable>

    @Query("UPDATE expiration_table SET time_stamp = :timeStamp WHERE entity = :entity")
    fun updateHeroesExpirationRow(entity: String = ExpirationEntity.HEROES.entityName,
                                    timeStamp: Long = System.currentTimeMillis()): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHeroesExpirationRow(expirationTable: ExpirationTable = ExpirationTable(
        ExpirationEntity.HEROES.entityName, System.currentTimeMillis())): Completable
}