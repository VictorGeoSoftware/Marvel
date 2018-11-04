package com.training.marvel.source

import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.training.marvel.source.di.AppComponent
import com.training.marvel.source.di.AppModule
import com.training.marvel.source.di.DaggerAppComponent

/**
 * Created by victor on 13/11/17.
 *
 */

class ParentApplication: MultiDexApplication() {

    val component:AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        // todo :: have a watch at this https://android.jlelse.eu/how-to-configure-multidex-in-an-application-android-f221198707ed
        MultiDex.install(this)
        component.inject(this)
    }
}