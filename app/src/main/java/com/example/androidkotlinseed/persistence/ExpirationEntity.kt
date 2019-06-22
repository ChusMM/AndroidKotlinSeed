package com.example.androidkotlinseed.persistence

enum class ExpirationEntity(val entityName: String) {
    HEROES("heroes");

    override fun toString(): String {
        return "ExpirationEntity(entityName='$entityName')"
    }
}