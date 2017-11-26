package com.test.marvel.vallettest.di.mainactivity

import com.test.marvel.vallettest.MainActivity
import dagger.Subcomponent
import javax.inject.Singleton

/**
 * Created by victor on 13/11/17.
 */

@Subcomponent(modules = arrayOf(MainActivityModule::class))
interface MainActivityComponent {
    fun inject(activity: MainActivity)
}