package com.example.androidkotlinseed

import android.app.Application
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.androidkotlinseed.api.CallError
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.domain.usecases.FetchHeroesUseCase
import com.example.androidkotlinseed.injection.DaggerUnitTestApplicationComponent
import com.example.androidkotlinseed.injection.UnitTestApplicationComponent
import com.example.androidkotlinseed.injection.UnitTestApplicationModule
import com.example.androidkotlinseed.injection.UnitTestUseCaseModule
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
    @Inject lateinit var fetchHeroesUseCase: FetchHeroesUseCase

    // System to test
    private lateinit var heroListViewModel: HeroListViewModel

    // Utililites
    private val unitTestUtils = UnitTestUtils()

    @Before
    fun setup() {
        val app: Application = mock(Application::class.java)
        `when`(app.applicationContext).thenReturn(app)

        testUnitApplicationComponent = DaggerUnitTestApplicationComponent.builder()
            .applicationModule(UnitTestApplicationModule(app))
            .useCaseModule(UnitTestUseCaseModule())
            .build()
        testUnitApplicationComponent.inject(this)

        heroListViewModel = HeroListViewModel(fetchHeroesUseCase)
        heroListViewModel.heroList.testObserver()
    }

    @After
    fun tearDown() {
        reset(fetchHeroesUseCase)
    }

    @Test
    fun heroListViewModel_fetchHeroes_success() {
        val result = dataFactory.superHeroesFromHeroListWrapper(unitTestUtils.heroListWrapper)
        this.fetchSucccess(result)

        heroListViewModel.fetchHeroesAndNotify()

        verify(fetchHeroesUseCase, times(1)).fetchAndNotify()
        verify(fetchHeroesUseCase, times(1)).onQueryHeroesOk(result)

        Truth.assertThat(heroListViewModel.heroList.value)
            .isEqualTo(result)
    }

    @Test
    fun heroListViewModel_fetchHeroes_fail() {
        this.fetchFailed()

        heroListViewModel.fetchHeroesAndNotify()

        verify(fetchHeroesUseCase, times(1)).fetchAndNotify()
        verify(fetchHeroesUseCase, times(1)).onQueryHeroesFailed(unitTestUtils.any(CallError::class.java))

        Truth.assertThat(heroListViewModel.heroList.value).isNull()
    }

    private fun fetchSucccess(result: List<SuperHero>) {
        doAnswer {
            fetchHeroesUseCase.onQueryHeroesOk(result)
        }.`when`(fetchHeroesUseCase).fetchAndNotify()
    }

    private fun fetchFailed() {
        doAnswer {
            fetchHeroesUseCase.onQueryHeroesFailed(CallError.UNKNOWN_ERROR)
        }.`when`(fetchHeroesUseCase).fetchAndNotify()
    }

    private fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
        observeForever(it)
    }
}