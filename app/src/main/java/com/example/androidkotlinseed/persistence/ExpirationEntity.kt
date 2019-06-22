package com.example.androidkotlinseed.persistence

import java.lang.IllegalArgumentException

@Suppress("RemoveRedundantQualifierName")
enum class ExpirationEntity(val entityName: String, val timeout: Long) {
    HEROES(ExpirationEntity.HEROES_ENTITY_NAME, 30 * 1000);

    companion object {
        const val HEROES_ENTITY_NAME = "heroes"

        fun buildFromString(entityName: String): ExpirationEntity {
            return when(entityName) {
                HEROES_ENTITY_NAME -> HEROES
                else -> throw IllegalArgumentException("Entity name not valid")
            }
        }
    }

    override fun toString(): String {
        return "ExpirationEntity(entityName='$entityName')"
    }
}