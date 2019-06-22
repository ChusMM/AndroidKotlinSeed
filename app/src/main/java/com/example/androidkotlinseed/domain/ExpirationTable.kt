package com.example.androidkotlinseed.domain

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "expiration_table", primaryKeys = ["entity"])
data class ExpirationTable(
    @ColumnInfo(name = "entity") val entity: String,
    @ColumnInfo(name = "time_stamp") val timeStamp: Long) {

    companion object {
        const val EXPIRATION_TIMEOUT: Long = 30 * 1000 // 30 seconds
    }

    fun isExpired(): Boolean = System.currentTimeMillis() - timeStamp >= EXPIRATION_TIMEOUT

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