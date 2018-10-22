package com.training.marvel.source.di.detailactivity

import com.training.marvel.source.DetailComicActivity
import dagger.Subcomponent

/**
 * Created by victor on 21/11/17.
 */
@Subcomponent(modules = arrayOf(DetailComicActivityModule::class))
interface DetailComicActivityComponent {
    fun inject(activity:DetailComicActivity)
}