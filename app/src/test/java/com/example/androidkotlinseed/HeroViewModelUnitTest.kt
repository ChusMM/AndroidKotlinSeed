package com.example.androidkotlinseed

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.domain.usecases.IFetchHeroesUseCase
import com.example.androidkotlinseed.injection.UnitTestApplicationComponent
import com.example.androidkotlinseed.mvvm.HeroListViewModel
import com.example.androidkotlinseed.mvvm.TestObserver
import com.example.androidkotlinseed.repository.DataFactory
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.reset
import org.powermock.api.mockito.PowerMockito.*
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import javax.inject.Inject

@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class)
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
    lateinit var lifeCycle: LifecycleRegistry

    // Utililites
    private val unitTestUtils = UnitTestUtils()

    @Before
    fun setup() {
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
    }

    @Test
    fun heroListViewModel_fetchHeroes_success() {
        val result = dataFactory.superHeroesFromHeroListWrapper(unitTestUtils.heroListWrapper)
        this.fetchSucccess(result)

        lifeCycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        verify(fetchHeroesUseCase, times(1)).fetchAndNotify()
        //verify(fetchHeroesUseCase, times(1)).onQueryHeroesOk(result)

        Truth.assertThat(heroListViewModel.heroList.value).isEqualTo(result)
    }

    @Test
    fun heroListViewModel_fetchHeroes_fail() {
        this.fetchFailed()

        lifeCycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        verify(fetchHeroesUseCase, times(1)).fetchAndNotify()
        verify(fetchHeroesUseCase, times(1)).onQueryHeroesFailed(unitTestUtils.any(CallError::class.java))

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