package com.example.androidkotlinseed.injection.application

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.androidkotlinseed.BuildConfig
import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import com.example.androidkotlinseed.domain.usecases.IFetchHeroesUseCase
import com.example.androidkotlinseed.persistence.AppDataBase
import com.example.androidkotlinseed.persistence.SuperHeroDao
import com.example.androidkotlinseed.repository.CacheManager
import com.example.androidkotlinseed.repository.DataFactory
import com.example.androidkotlinseed.repository.DataSource
import com.example.androidkotlinseed.repository.DataSourceProvider
import com.example.androidkotlinseed.repository.DataStrategy
import com.example.androidkotlinseed.repository.DataWebService
import com.example.androidkotlinseed.utils.AppRxSchedulers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
open class UseCaseModule {
    companion object {
        private const val BASE_URL = BuildConfig.API_BASE_URL
        private const val CONNECT_TIMEOUT: Long = 10
        private const val READ_TIMEOUT: Long = 10
        private const val HEROES_DB = "heroes_database"
    }

    @Singleton
    @Provides
    open fun getRetrofit(): Retrofit.Builder {
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
        return DataSourceProvider().getDefaultDataSource()
    }

    @Singleton
    @Provides
    open fun getAppDataBase(application: Application): AppDataBase {
        return Room.databaseBuilder(application, AppDataBase::class.java, HEROES_DB).build()
    }

    @Singleton
    @Provides
    open fun getSuperHeroDao(appDataBase: AppDataBase): SuperHeroDao {
        return appDataBase.superHeroDao()
    }

    @Singleton
    @Provides
    open fun getCacheManager(superHeroDao: SuperHeroDao,
                             appRxSchedulers: AppRxSchedulers): CacheManager {
        return CacheManager(superHeroDao, appRxSchedulers)
    }

    @Provides
    open fun getDataStrategy(marvelApi: MarvelApi,
                             dataFactory: DataFactory,
                             cacheManager: CacheManager,
                             appRxSchedulers: AppRxSchedulers): DataStrategy {
        return DataWebService(marvelApi, dataFactory, appRxSchedulers, cacheManager)
    }

    @Provides
    open fun getFetchHeroesUserCase(dataStrategy: DataStrategy, context: Context)
            : IFetchHeroesUseCase {
        return FetchHeroesUseCase(dataStrategy, context)
    }
}