package com.training.marvel.source.di

import android.app.Application
import com.training.marvel.source.di.detailactivity.DetailComicActivityComponent
import com.training.marvel.source.di.detailactivity.DetailComicActivityModule
import com.training.marvel.source.di.mainactivity.MainActivityComponent
import com.training.marvel.source.di.mainactivity.MainActivityModule
import com.training.marvel.source.presenters.MarvelInteractorImpl
import dagger.Component
import javax.inject.Singleton

/**
 * Created by victor on 13/11/17.
 *
 */

@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, PresenterModule::class))
interface AppComponent {
    fun inject(application: Application)
    fun inject(marvelInteractorImpl: MarvelInteractorImpl)
    fun plus(mainActivityModule: MainActivityModule):MainActivityComponent
    fun plus(detailActivityModule: DetailComicActivityModule):DetailComicActivityComponent
}