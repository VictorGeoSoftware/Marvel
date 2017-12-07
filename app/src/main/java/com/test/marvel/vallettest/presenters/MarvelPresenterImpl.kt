package com.test.marvel.vallettest.presenters

import android.content.Context
import com.test.marvel.vallettest.models.Comic

/**
 * Created by victor on 14/11/17.
 *
 */

class MarvelPresenterImpl:MarvelPresenter, MarvelInteractor.RequestListener {
    var marvelView: MarvelView? = null
    var marvelInteractor:MarvelInteractor? = null

    constructor(context: Context) {
        this.marvelInteractor = MarvelInteractorImpl(context)
    }


    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ PRESENTER INERFACE ------------------------------------------------------------
    override fun setView(marvelView: MarvelView) {
        this.marvelView = marvelView
    }

    override fun getSuperHeroComics() {
        marvelView?.showProgressBar()
        marvelInteractor?.getSuperHeroComics(this)
    }

    override fun getComicDetail(comicId: Long) {
        marvelView?.showProgressBar()
        marvelInteractor?.getComicDetail(comicId, this)
    }

    override fun onDestroy() {
        marvelInteractor?.disposeObservables()
    }



    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ INTERACTOR INERFACE -----------------------------------------------------------
    override fun onComicListReceived(comicList:ArrayList<Comic>?) {
        if (comicList != null) {
            marvelView?.onSuperHeroComicsReceived(comicList)
        }
    }

    override fun onComicListError(error:String) {
        marvelView?.onSuperHeroComicsError(error)
    }

    override fun onComicDetailsReceived(comic:Comic) {
        marvelView?.onComicDetailReceived(comic)
    }

    override fun onComicDetailError(error:String) {
        marvelView?.onComicDetailError(error)
    }
}