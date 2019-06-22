package com.example.androidkotlinseed.injection

import android.app.Application
import android.content.Context
import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.injection.application.UseCaseModule
import com.example.androidkotlinseed.persistence.AppDataBase
import com.example.androidkotlinseed.persistence.SuperHeroDao
import com.example.androidkotlinseed.repository.CacheManager
import com.example.androidkotlinseed.repository.DataFactory
import com.example.androidkotlinseed.repository.DataSource
import com.example.androidkotlinseed.repository.DataStrategy
import org.powermock.api.mockito.PowerMockito.mock
import retrofit2.Retrofit

class UnitTestUseCaseModule : UseCaseModule() {
    override fun getMarvelApi(retrofitBuilder: Retrofit.Builder): MarvelApi {
        return mock(MarvelApi::class.java)
    }

    override fun getAppDataBase(application: Application): AppDataBase {
        return mock(AppDataBase::class.java)
    }

    override fun getSuperHeroDao(appDataBase: AppDataBase): SuperHeroDao {
        return mock(SuperHeroDao::class.java)
    }

    override fun getCacheManager(superHeroDao: SuperHeroDao): CacheManager {
        return mock(CacheManager::class.java)
    }

    override fun getDataStrategy(
        dataSource: DataSource,
        marvelApi: MarvelApi,
        cacheManager: CacheManager,
        dataFactory: DataFactory,
        context: Context
    ): DataStrategy {
        return mock(DataStrategy::class.java)
    }
}