package com.example.androidkotlinseed.repository

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.androidkotlinseed.RxSchedulerRule
import com.example.androidkotlinseed.UnitTestMockServerDispatcher
import com.example.androidkotlinseed.UnitTestUtils
import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.argumentCaptor
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.injection.UnitTestApplicationComponent
import com.example.androidkotlinseed.utils.AppRxSchedulers
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
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

    @Inject lateinit var marvelApi: MarvelApi
    @Inject lateinit var dataFactory: DataFactory
    @Inject lateinit var cacheManager: CacheManager
    @Inject lateinit var appRxSchedulers: AppRxSchedulers

    // SUT
    private lateinit var dataWebService: DataStrategy

    @Before
    fun setup() {
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
    fun dataWebService_retrieve_ok() {
        mockWebServer.dispatcher = UnitTestMockServerDispatcher().RequestDispatcher()

        val listener = mock(DataStrategy.QueryHeroesListener::class.java)
        val captor = argumentCaptor<List<SuperHero>>()

        dataWebService.queryHeroes(listener)

        verify(listener, times(1)).onQueryHeroesOk(captor.capture())
        verify(listener, times(0)).onQueryHeroesFailed(unitTestUtils.any())
    }

    @Test
    fun dataWebService_retrieve_fail() {
        mockWebServer.dispatcher = UnitTestMockServerDispatcher().RequestErrorDispatcher()

        val listener = mock(DataStrategy.QueryHeroesListener::class.java)
        val captor = argumentCaptor<CallError>()

        doAnswer {
            dataWebService.evaluateHeroesCacheAccess(true, listener, CallError.FORBIDDEN)
        }.`when`(cacheManager).checkHeroesCacheValidity(unitTestUtils.any())

        dataWebService.queryHeroes(listener)

        verify(listener, times(0)).onQueryHeroesOk(unitTestUtils.any())
        verify(listener, timeout(5000)).onQueryHeroesFailed(captor.capture())
    }
}