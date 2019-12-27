package com.example.androidkotlinseed.repository

import android.util.Log
import com.example.androidkotlinseed.BuildConfig
import com.example.androidkotlinseed.repository.DataSource.Companion.MOCK_DATA_ORIGIN
import com.example.androidkotlinseed.repository.DataSource.Companion.WS_DATA_ORIGIN

@Suppress("RemoveRedundantQualifierName")
class DataSourceProvider {
    private val TAG = DataSourceProvider::class.qualifiedName

    fun getDefaultDataSource(): DataSource {
        return buildFromString(BuildConfig.DATA_SOURCE)
    }

    fun buildFromString(source: String): DataSource {
        return when (source) {
            WS_DATA_ORIGIN   -> DataSource.DATA_WS
            MOCK_DATA_ORIGIN -> DataSource.DATA_MOCK
            else             -> {
                Log.d(TAG, "Data source specified not found, using data mock...")
                DataSource.DATA_MOCK
            }
        }
    }
}