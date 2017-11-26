package com.test.marvel.vallettest.di.detailactivity

import com.test.marvel.vallettest.DetailComicActivity
import dagger.Subcomponent

/**
 * Created by victor on 21/11/17.
 */
@Subcomponent(modules = arrayOf(DetailComicActivityModule::class))
interface DetailComicActivityComponent {
    fun inject(activity:DetailComicActivity)
}