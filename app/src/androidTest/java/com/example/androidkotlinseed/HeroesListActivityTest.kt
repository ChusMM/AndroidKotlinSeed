package com.example.androidkotlinseed

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.androidkotlinseed.view.activities.HeroDetailActivity
import com.example.androidkotlinseed.view.activities.HeroesListActivity
import com.example.androidkotlinseed.view.adapters.HeroViewHolder
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HeroesListActivityTest {
    @get:Rule
    val activityRule: ActivityTestRule<HeroesListActivity> = ActivityTestRule(HeroesListActivity::class.java, false, false)

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        Intents.init()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        Intents.release()
    }

    @Test
    fun heroListActivity_clickFirst_Ok() {
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        activityRule.launchActivity(Intent())

        Espresso.onView(ViewMatchers.withId(R.id.recycler_heroes)).perform(
                RecyclerViewActions.actionOnItemAtPosition<HeroViewHolder>(0, ViewActions.click())
        )

        Intents.intended(IntentMatchers.hasComponent(HeroDetailActivity::class.java.name))
    }
}
