package com.example.androidkotlinseed.repository

import android.util.Log
import com.example.androidkotlinseed.BuildConfig

enum class DataSource(private val dataSource: String) {
    DATA_WS("ws"),
    DATA_MOCK("mock");

    override fun toString(): String{
        return "DataSource(dataSource='$dataSource')"
    }

    companion object {
        private val TAG = DataSource::class.qualifiedName
        private const val WS_DATA_ORIGIN = "WS"
        private const val MOCK_DATA_ORIGIN = "MOCK"

        fun getDefaultDataSource(): DataSource {
            return buildFromString(BuildConfig.DATA_SOURCE)
        }

        fun buildFromString(source: String): DataSource {
            return when(source) {
                WS_DATA_ORIGIN -> DATA_WS
                MOCK_DATA_ORIGIN -> DATA_MOCK
                else -> {
                    Log.d(TAG, "Data source specified not found, using data mock...")
                    DATA_MOCK
                }
            }
        }
    }
}