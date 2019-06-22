package com.example.androidkotlinseed.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.example.androidkotlinseed.persistence.ExpirationEntity

@Entity(tableName = "expiration_table", primaryKeys = ["entity"])
data class ExpirationTable(
    @ColumnInfo(name = "entity") val entity: String,
    @ColumnInfo(name = "time_stamp") val timeStamp: Long) {

    @Ignore
    private val expirationTimeout = ExpirationEntity.buildFromString(entity).timeout

    fun isExpired(): Boolean = System.currentTimeMillis() - timeStamp >= expirationTimeout

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExpirationTable

        if (entity != other.entity) return false

        return true
    }

    override fun hashCode(): Int {
        return entity.hashCode()
    }
}