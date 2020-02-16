package com.example.androidkotlinseed.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.injection.BaseActivity
import com.example.androidkotlinseed.repository.DataSource
import com.example.androidkotlinseed.utils.StartupMessage
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

open class SplashActivity : BaseActivity() {
    @Inject
    lateinit var dataSource: DataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPresentationComponent().inject(this)

        if (dataSource == DataSource.DATA_WS) {
            Thread(Runnable {
                Thread.sleep(1500)
                EventBus.getDefault().postSticky(StartupMessage(ready = true))
            }).start()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(startupMessage: StartupMessage) {
        if (startupMessage.ready) {
            val intent = Intent(this, HeroesListActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, R.string.server_error_dialog_title, Toast.LENGTH_LONG).show()
        }
        this.finish()
    }
}
