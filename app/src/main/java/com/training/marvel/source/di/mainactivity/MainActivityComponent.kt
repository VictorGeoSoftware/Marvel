package com.training.marvel.source.di.mainactivity

import com.training.marvel.source.MainActivity
import dagger.Subcomponent

/**
 * Created by victor on 13/11/17.
 *
 */

@Subcomponent(modules = arrayOf(MainActivityModule::class))
interface MainActivityComponent {
    fun inject(activity: MainActivity)
}