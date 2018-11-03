package com.training.marvel.source.di

import com.training.marvel.source.presenters.MarvelPresenter
import com.training.marvel.source.presenters.MarvelPresenterImpl
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
    fun provideMarvelPresenter():MarvelPresenter = MarvelPresenterImpl()
}