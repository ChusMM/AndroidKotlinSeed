package com.example.androidkotlinseed

import android.annotation.SuppressLint
import android.util.Log
import com.example.androidkotlinseed.injection.application.ApplicationModule
import com.example.androidkotlinseed.injection.application.DaggerMockApplicationComponent
import com.example.androidkotlinseed.injection.application.MockApplicationComponent
import com.example.androidkotlinseed.injection.application.UseCaseModule
import com.example.androidkotlinseed.repository.mock.MockServerDispatcher
import com.example.androidkotlinseed.utils.AppRxSchedulers
import com.example.androidkotlinseed.utils.StartupMessage
import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.disposables.Disposable
import okhttp3.mockwebserver.MockWebServer
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@SuppressLint("Registered")
class MockApp : App() {
    private val TAG = MockApp::class.java.simpleName

    @Inject
    lateinit var appRxSchedulers: AppRxSchedulers
    @Inject
    lateinit var mockWebServer: MockWebServer

    private var disposable: Disposable? = null

    override fun onCreate() {
        super.onCreate()
        (this.getApplicationComponent() as MockApplicationComponent).inject(this)
        disposable = startMockWebServer().subscribe({
            Log.d(TAG, "Mock web server started")
            disposable?.dispose()
            EventBus.getDefault().postSticky(StartupMessage(ready = true))
        }, { error ->
            Log.d(TAG, error.toString())
            EventBus.getDefault().postSticky(StartupMessage(ready = false))
        })
    }

    override fun buidAppComponent(): MockApplicationComponent {
        return DaggerMockApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .useCaseModule(UseCaseModule())
                .build()
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