package com.example.androidkotlinseed

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.example.androidkotlinseed.api.HeroListWrapper
import com.example.androidkotlinseed.injection.DaggerUnitTestApplicationComponent
import com.example.androidkotlinseed.injection.UnitTestApplicationComponent
import com.example.androidkotlinseed.injection.UnitTestApplicationModule
import com.example.androidkotlinseed.injection.UnitTestUseCaseModule
import com.example.androidkotlinseed.repository.DataMock.Companion.mockHeroesJson
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito

@Suppress("MemberVisibilityCanBePrivate")
class UnitTestUtils {

    fun <T> any(t: Class<out T>): T {
        return Mockito.any(t)
    }

    inline fun <reified T : Any> argumentCaptor() = ArgumentCaptor.forClass(T::class.java)

    fun getMockApp(): Application {
        val app: Application = PowerMockito.mock(Application::class.java)
        PowerMockito.`when`(app.applicationContext).thenReturn(app)

        return app
    }

    fun createTestApplicationComponent(): UnitTestApplicationComponent {
        return DaggerUnitTestApplicationComponent.builder()
            .applicationModule(UnitTestApplicationModule(getMockApp()))
            .useCaseModule(UnitTestUseCaseModule())
            .build()
    }

    fun createLifecycleOwner(): LifecycleRegistry {
        val lifeCycleOwner: LifecycleOwner = PowerMockito.mock(LifecycleOwner::class.java)
        return LifecycleRegistry(lifeCycleOwner)
    }

    val heroListWrapper = HeroListWrapper.fromJson(mockHeroesJson)
}