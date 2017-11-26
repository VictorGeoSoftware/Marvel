package com.test.marvel.vallettest.di

import android.app.Application
import com.test.marvel.vallettest.di.detailactivity.DetailComicActivityComponent
import com.test.marvel.vallettest.di.detailactivity.DetailComicActivityModule
import com.test.marvel.vallettest.di.mainactivity.MainActivityComponent
import com.test.marvel.vallettest.di.mainactivity.MainActivityModule
import com.test.marvel.vallettest.presenters.MarvelInteractorImpl
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