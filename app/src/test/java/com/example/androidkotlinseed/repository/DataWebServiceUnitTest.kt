package com.example.androidkotlinseed.repository

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.androidkotlinseed.RxSchedulerRule
import com.example.androidkotlinseed.UnitTestUtils
import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.argumentCaptor
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.injection.UnitTestApplicationComponent
import com.example.androidkotlinseed.UnitTestMockServerDispatcher
import com.example.androidkotlinseed.utils.AppRxSchedulers
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import javax.inject.Inject

@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class)
@PowerMockIgnore("javax.net.ssl.*")
class DataWebServiceUnitTest {
    private lateinit var testUnitApplicationComponent: UnitTestApplicationComponent

    // For archiecture components
    @Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    // Config Trampoline for Rx
    @Rule
    val rxSchedulerRule = RxSchedulerRule()

    // Utililites
    private val unitTestUtils = UnitTestUtils()
    private lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var marvelApi: MarvelApi
    @Inject
    lateinit var dataFactory: DataFactory
    @Inject
    lateinit var cacheManager: CacheManager
    @Inject
    lateinit var appRxSchedulers: AppRxSchedulers

    // SUT
    private lateinit var dataWebService: DataStrategy

    @Before
    fun setup() {
        PowerMockito.mockStatic(Log::class.java)

        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        testUnitApplicationComponent = unitTestUtils.createTestApplicationComponent()
        testUnitApplicationComponent.inject(this)

        dataWebService = DataWebService(marvelApi, dataFactory, appRxSchedulers, cacheManager)
    }

    @After
    fun tearDown() {
        reset(cacheManager)
        mockWebServer.shutdown()
    }

    @Test
    fun dataWebService_retrieveOk_saveOk() {
        mockWebServer.dispatcher = UnitTestMockServerDispatcher().RequestDispatcher()

        val listener = mock(DataStrategy.QueryHeroesListener::class.java)
        val captor = argumentCaptor<List<SuperHero>>()

        `when`(cacheManager.saveHeroes(unitTestUtils.any())).thenReturn(Completable.create { emitter ->
            emitter.onComplete()
        })

        dataWebService.queryHeroes(listener)

        verify(cacheManager, times(1)).saveHeroes(unitTestUtils.any())
        verify(listener, times(1)).onQueryHeroesOk(captor.capture())
        verify(listener, times(0)).onQueryHeroesFailed(unitTestUtils.any())
    }

    @Test
    fun dataWebService_retrieveOk_saveFail() {
        mockWebServer.dispatcher = UnitTestMockServerDispatcher().RequestDispatcher()

        val listener = mock(DataStrategy.QueryHeroesListener::class.java)
        val captor = argumentCaptor<List<SuperHero>>()

        `when`(cacheManager.saveHeroes(unitTestUtils.any())).thenReturn(Completable.create { emitter ->
            emitter.onError(Throwable())
        })

        dataWebService.queryHeroes(listener)

        verify(cacheManager, times(1)).saveHeroes(unitTestUtils.any())
        verify(listener, times(1)).onQueryHeroesOk(captor.capture())
        verify(listener, times(0)).onQueryHeroesFailed(unitTestUtils.any())
    }

    @Test
    fun dataWebService_retrieveFail_cacheOk() {
        mockWebServer.dispatcher = UnitTestMockServerDispatcher().RequestErrorDispatcher()

        val listener = mock(DataStrategy.QueryHeroesListener::class.java)
        val heroListCaptor = argumentCaptor<List<SuperHero>>()
        val callErrorCaptor = argumentCaptor<CallError>()

        `when`(cacheManager.checkHeroesCacheValidity()).thenReturn(Observable.create { emitter ->
            emitter.onNext(false)
        })

        `when`(cacheManager.queryHeroesFromCache()).thenReturn(Observable.create { emitter ->
            emitter.onNext(listOf())
        })

        dataWebService.queryHeroes(listener)

        verify(cacheManager, times(1)).checkHeroesCacheValidity()
        verify(listener, times(1)).onQueryHeroesOk(heroListCaptor.capture())
        verify(listener, times(0)).onQueryHeroesFailed(callErrorCaptor.capture())
    }

    @Test
    fun dataWebService_retrieveFail_cacheExpired() {
        mockWebServer.dispatcher = UnitTestMockServerDispatcher().RequestErrorDispatcher()

        val listener = mock(DataStrategy.QueryHeroesListener::class.java)
        val captor = argumentCaptor<CallError>()

        `when`(cacheManager.checkHeroesCacheValidity()).thenReturn(Observable.create { emitter ->
            emitter.onNext(true)
        })

        dataWebService.queryHeroes(listener)

        verify(cacheManager, times(1)).checkHeroesCacheValidity()
        verify(listener, times(0)).onQueryHeroesOk(unitTestUtils.any())
        verify(listener, times(1)).onQueryHeroesFailed(captor.capture())
    }
}