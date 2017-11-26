package com.test.marvel.vallettest.di

import android.content.Context
import com.test.marvel.vallettest.presenters.MarvelPresenter
import com.test.marvel.vallettest.presenters.MarvelPresenterImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by victor on 14/11/17.
 *
 */

@Module
class PresenterModule {

    @Provides
    @Singleton
    fun provideMarvelPresenter(context: Context):MarvelPresenter = MarvelPresenterImpl(context)
}