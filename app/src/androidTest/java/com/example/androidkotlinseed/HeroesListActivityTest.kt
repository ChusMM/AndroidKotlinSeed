@file:Suppress("DEPRECATION")

package com.example.androidkotlinseed

import androidx.test.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.androidkotlinseed.view.activities.HeroesListActivity
import okhttp3.mockwebserver.MockWebServer
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(PowerMockRunner::class)
class HeroesListActivityTest {
    @Rule
    val testRule: ActivityTestRule<HeroesListActivity> = ActivityTestRule(HeroesListActivity::class.java, false, false)

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.example.androidkotlinseed", appContext.packageName)
    }
}
