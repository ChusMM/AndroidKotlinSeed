package com.example.androidkotlinseed.domain

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "super_heroes")
data class SuperHero(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "photo") val photo: String?,
    @ColumnInfo(name = "real_name") val realName: String?,
    @ColumnInfo(name = "height") val height: String?,
    @ColumnInfo(name = "power") val power: String?,
    @ColumnInfo(name = "abilities") val abilities: String?,
    @ColumnInfo(name = "groups") val groups: String?) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SuperHero

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(uid)
        writeString(name)
        writeString(photo)
        writeString(realName)
        writeString(height)
        writeString(power)
        writeString(abilities)
        writeString(groups)
    }

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<SuperHero> = object : Parcelable.Creator<SuperHero> {
            override fun createFromParcel(source: Parcel): SuperHero =
                SuperHero(source)
            override fun newArray(size: Int): Array<SuperHero?> = arrayOfNulls(size)
        }
    }
}