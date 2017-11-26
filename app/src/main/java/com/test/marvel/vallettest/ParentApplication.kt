package com.test.marvel.vallettest

import android.app.Application
import com.test.marvel.vallettest.di.AppComponent
import com.test.marvel.vallettest.di.AppModule
import com.test.marvel.vallettest.di.DaggerAppComponent

/**
 * Created by victor on 13/11/17.
 *
 */

class ParentApplication:Application() {

    val component:AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }


    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}