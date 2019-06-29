package com.example.androidkotlinseed.mvvm

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import com.example.androidkotlinseed.RxSchedulerRule
import com.example.androidkotlinseed.UnitTestMockServerDispatcher
import com.example.androidkotlinseed.UnitTestUtils
import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.domain.usecases.IFetchHeroesUseCase
import com.example.androidkotlinseed.injection.UnitTestApplicationComponent
import com.example.androidkotlinseed.repository.DataFactory
import com.google.common.truth.Truth
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
class HeroViewModelUnitTest {
    private lateinit var testUnitApplicationComponent: UnitTestApplicationComponent

    // For archiecture components
    @Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    // Config Trampoline for Rx
    @Rule
    val rxSchedulerRule = RxSchedulerRule()

    @Inject lateinit var dataFactory: DataFactory
    @Inject lateinit var fetchHeroesUseCase: IFetchHeroesUseCase

    // System to test
    private lateinit var heroListViewModel: HeroListViewModel
    private lateinit var lifeCycle: LifecycleRegistry

    // Utililites
    private val unitTestUtils = UnitTestUtils()
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        testUnitApplicationComponent = unitTestUtils.createTestApplicationComponent()
        testUnitApplicationComponent.inject(this)

        heroListViewModel = HeroListViewModel(fetchHeroesUseCase)
        heroListViewModel.heroList.testObserver()

        lifeCycle = unitTestUtils.createLifecycleOwner()
        lifeCycle.addObserver(heroListViewModel)
    }

    @After
    fun tearDown() {
        lifeCycle.removeObserver(heroListViewModel)
        reset(fetchHeroesUseCase)
        mockWebServer.shutdown()
    }

    @Test
    fun heroListViewModel_fetchHeroes_success() {
        mockWebServer.dispatcher = UnitTestMockServerDispatcher().RequestDispatcher()

        val result = dataFactory.superHeroesFromHeroListWrapper(unitTestUtils.heroListWrapper)
        this.fetchSucccess(result)

        lifeCycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        verify(fetchHeroesUseCase, times(1)).fetchAndNotify()
        //verify(fetchHeroesUseCase, times(1)).onQueryHeroesOk(result)

        Truth.assertThat(heroListViewModel.heroList.value).isEqualTo(result)
    }

    @Test
    fun heroListViewModel_fetchHeroes_fail() {
        mockWebServer.dispatcher = UnitTestMockServerDispatcher().RequestDispatcher()

        this.fetchFailed()

        lifeCycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        verify(fetchHeroesUseCase, times(1)).fetchAndNotify()
        verify(fetchHeroesUseCase, times(1)).onQueryHeroesFailed(unitTestUtils.any())

        Truth.assertThat(heroListViewModel.heroList.value).isNull()
    }

    private fun fetchSucccess(result: List<SuperHero>) {
        doAnswer {
            fetchHeroesUseCase.onQueryHeroesOk(result)
            heroListViewModel.onFetchHeroesOk(result)
        }.`when`(fetchHeroesUseCase).fetchAndNotify()
    }

    private fun fetchFailed() {
        doAnswer {
            fetchHeroesUseCase.onQueryHeroesFailed(CallError.UNKNOWN_ERROR)
            heroListViewModel.onFetchHeroesFailed("Error")
        }.`when`(fetchHeroesUseCase).fetchAndNotify()
    }

    private fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
        observeForever(it)
    }
}