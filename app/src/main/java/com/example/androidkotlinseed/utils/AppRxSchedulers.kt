package com.example.androidkotlinseed.utils

import io.reactivex.Scheduler

class AppRxSchedulers(val database: Scheduler,
                      val disk: Scheduler,
                      val network: Scheduler,
                      val main: Scheduler)
