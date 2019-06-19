package com.example.androidkotlinseed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.androidkotlinseed.view.activities.HeroesListActivityMvc

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            val intent = Intent(this, HeroesListActivityMvc::class.java)
            startActivity(intent)
            finish()
        }, 2500)
    }
}
