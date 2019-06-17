package com.example.androidkotlinseed.injection

import android.app.Application
import com.example.androidkotlinseed.injection.application.ApplicationModule

class AndroidTestApplicationModule(application: Application) : ApplicationModule(application)