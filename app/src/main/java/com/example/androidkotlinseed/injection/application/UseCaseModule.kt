package com.example.androidkotlinseed.injection.application

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.androidkotlinseed.BuildConfig
import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.persistence.AppDataBase
import com.example.androidkotlinseed.persistence.SuperHeroDao
import com.example.androidkotlinseed.repository.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class UseCaseModule {
    companion object {
        private const val BASE_URL = BuildConfig.API_BASE_URL
        private const val CONNECT_TIMEOUT: Long = 10
        private const val READ_TIMEOUT: Long = 10
    }

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    @Singleton
    @Provides
    open fun getMarvelApi(retrofitBuilder: Retrofit.Builder): MarvelApi {
        val httpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

        val httpClient = httpClientBuilder.build()
        val retrofit = retrofitBuilder.client(httpClient).build()

        return retrofit.create(MarvelApi::class.java)
    }

    @Singleton
    @Provides
    fun getDataFactory(): DataFactory {
        return DataFactory()
    }

    @Singleton
    @Provides
    fun getDataSource(): DataSource {
        return DataSource.getDefaultDataSource()
    }

    @Singleton
    @Provides
    open fun getAppDataBase(application: Application): AppDataBase {
        return Room.databaseBuilder(application,
            AppDataBase::class.java, "heroes_database").build()
    }

    @Singleton
    @Provides
    open fun getSuperHeroDao(appDataBase: AppDataBase): SuperHeroDao {
        return appDataBase.superHeroDao()
    }

    @Provides
    open fun getCacheManager(superHeroDao: SuperHeroDao, context: Context): CacheManager {
        return CacheManager(superHeroDao, context)
    }

    @Provides
    open fun getDataStrategy(dataSource: DataSource,
                        marvelApi: MarvelApi,
                        cacheManager: CacheManager,
                        dataFactory: DataFactory,
                        context: Context): DataStrategy {
        return when(dataSource) {
            DataSource.DATA_WS -> DataWebService(marvelApi, dataFactory, cacheManager)
            DataSource.DATA_MOCK -> DataWebService(marvelApi, dataFactory, cacheManager)
            else -> {
                DataWebService(marvelApi, dataFactory, cacheManager)
            }
        }
    }
}