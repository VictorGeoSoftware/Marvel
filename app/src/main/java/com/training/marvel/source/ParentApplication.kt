package com.training.marvel.source

import android.app.Application
import com.training.marvel.source.di.AppComponent
import com.training.marvel.source.di.AppModule
import com.training.marvel.source.di.DaggerAppComponent

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