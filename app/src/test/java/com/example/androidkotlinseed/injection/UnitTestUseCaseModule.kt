package com.example.androidkotlinseed.injection

import android.app.Application
import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.injection.application.UseCaseModule
import com.example.androidkotlinseed.persistence.AppDataBase
import com.example.androidkotlinseed.persistence.SuperHeroDao
import com.example.androidkotlinseed.repository.CacheManager
import com.example.androidkotlinseed.repository.DataFactory
import com.example.androidkotlinseed.repository.DataStrategy
import com.example.androidkotlinseed.utils.AppRxSchedulers
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class UnitTestUseCaseModule : UseCaseModule() {
    companion object {
        private const val BASE_URL = "http://localhost:8080/"
    }

    override fun getRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    override fun getAppDataBase(application: Application): AppDataBase {
        return mock(AppDataBase::class.java)
    }

    override fun getSuperHeroDao(appDataBase: AppDataBase): SuperHeroDao {
        return mock(SuperHeroDao::class.java)
    }

    override fun getCacheManager(superHeroDao: SuperHeroDao, appRxSchedulers: AppRxSchedulers): CacheManager {
        return mock(CacheManager::class.java)
    }

    override fun getDataStrategy(marvelApi: MarvelApi,
                                 dataFactory: DataFactory,
                                 cacheManager: CacheManager,
                                 appRxSchedulers: AppRxSchedulers): DataStrategy {
        return mock(DataStrategy::class.java)
    }
}