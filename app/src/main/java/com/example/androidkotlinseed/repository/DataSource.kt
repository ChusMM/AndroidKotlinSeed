package com.example.androidkotlinseed.repository

@Suppress("RemoveRedundantQualifierName")
enum class DataSource(private val dataSource: String) {
    DATA_WS(DataSource.WS_DATA_ORIGIN),
    DATA_MOCK(DataSource.MOCK_DATA_ORIGIN);

    companion object {
        const val WS_DATA_ORIGIN = "WS"
        const val MOCK_DATA_ORIGIN = "MOCK"
    }

    override fun toString(): String {
        return "DataSource(dataSource='$dataSource')"
    }
}