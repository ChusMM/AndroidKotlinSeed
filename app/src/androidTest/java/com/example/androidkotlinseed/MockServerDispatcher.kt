package com.example.androidkotlinseed

import com.example.androidkotlinseed.api.MarvelApi
import com.example.androidkotlinseed.repository.DataMock.Companion.mockHeroesJson
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockServerDispatcher {
    /**
     * Return ok response from mock server
     */
    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when {
                request.path == MarvelApi.GET_HEROES_PATH -> MockResponse().setResponseCode(200).setBody(mockHeroesJson)
                else                                      -> MockResponse().setResponseCode(404)
            }
        }
    }

    /**
     * Return error response from mock server
     */
    internal inner class ErrorDispatcher : Dispatcher() {

        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400)
        }
    }
}