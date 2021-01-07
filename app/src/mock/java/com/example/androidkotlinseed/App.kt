package com.example.androidkotlinseed

import android.app.Application
import android.util.Log
import com.example.androidkotlinseed.repository.mock.MockServerDispatcher
import com.example.androidkotlinseed.utils.AppRxSchedulers
import com.example.androidkotlinseed.utils.StartupMessage
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.disposables.Disposable
import okhttp3.mockwebserver.MockWebServer
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    private val TAG = App::class.java.simpleName

    @Inject
    lateinit var appRxSchedulers: AppRxSchedulers
    @Inject
    lateinit var mockWebServer: MockWebServer

    private var disposable: Disposable? = null

    override fun onCreate() {
        super.onCreate()
        disposable = startMockWebServer().subscribe({
            Log.d(TAG, "Mock web server started")
            disposable?.dispose()
            EventBus.getDefault().postSticky(StartupMessage(ready = true))
        }, { error ->
            Log.d(TAG, error.toString())
            EventBus.getDefault().postSticky(StartupMessage(ready = false))
        })
    }

    override fun onTerminate() {
        super.onTerminate()
        disposable = stopMockWebServer().subscribe({
            Log.d(TAG, "Mock web server stopped")
            disposable?.dispose()
        }, { error ->
            Log.d(TAG, error.toString())
        })
    }

    /**
     * Restart mock Webserver
     */
    private fun startMockWebServer(): Completable {
        val handler = CompletableOnSubscribe { emitter ->
            val emitterDisposable = appRxSchedulers.network.createWorker().schedule {
                try {
                    mockWebServer = MockWebServer()
                    mockWebServer.start(8080)
                    mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
                    if (!emitter.isDisposed) emitter.onComplete()
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                    if (!emitter.isDisposed) emitter.onError(e)
                }

            }
            emitter.setDisposable(emitterDisposable)
        }
        return Completable.create(handler).observeOn(appRxSchedulers.network)
    }

    /**
     * Stop mock web server
     */
    private fun stopMockWebServer(): Completable {
        val handler = CompletableOnSubscribe { emitter ->
            val emitterDisposable = appRxSchedulers.network.createWorker().schedule {
                try {
                    mockWebServer.shutdown()
                    if (!emitter.isDisposed) emitter.onComplete()
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                    if (!emitter.isDisposed) emitter.onError(e)
                }
            }
            emitter.setDisposable(emitterDisposable)
        }
        return Completable.create(handler).observeOn(appRxSchedulers.network)
    }
}