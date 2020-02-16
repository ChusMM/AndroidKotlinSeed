package com.example.androidkotlinseed

import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.repository.mock.Mocks.Companion.mockHeroesJson
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_OK

class UnitTestMockServerDispatcher {
    /**
     * Return ok response from mock server
     */
    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                MarvelApi.GET_HEROES_PATH -> MockResponse().setResponseCode(HTTP_OK).setBody(mockHeroesJson)
                else                      -> MockResponse().setResponseCode(404)
            }
        }
    }

    internal inner class RequestErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                MarvelApi.GET_HEROES_PATH -> MockResponse().setResponseCode(HTTP_FORBIDDEN).setBody("forbidden")
                else                      -> MockResponse().setResponseCode(404)
            }
        }
    }
}