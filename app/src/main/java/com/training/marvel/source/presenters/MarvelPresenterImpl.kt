package com.training.marvel.source.presenters

import arrow.data.ReaderApi
import arrow.data.flatMap
import arrow.data.map
import com.training.marvel.source.context.ComicsContext
import com.training.marvel.source.models.Comic

/**
 * Created by victor on 14/11/17.
 *
 */

class MarvelPresenterImpl : MarvelPresenter, MarvelInteractor.RequestListener {
    private var marvelView: MarvelView? = null
    private var marvelInteractor: MarvelInteractor = MarvelInteractorImpl()


    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ PRESENTER INERFACE ------------------------------------------------------------
    override fun setView(marvelView: MarvelView) {
        this.marvelView = marvelView
    }

    override fun getSuperHeroComics() = ReaderApi.ask<ComicsContext.GetComicContext>().flatMap { (_, view: MarvelView) ->
        view.showProgressBar()
        marvelInteractor.getSuperHeroComics().map { io ->
            io.unsafeRunAsync {
                it.map { maybeComics ->
                    maybeComics.fold(
                            { error ->  view.onSuperHeroComicsError(error) },
                            { success -> view.onSuperHeroComicsReceived(success) })
                }
            }
        }
    }

    override fun getComicDetail(comicId: Long) {
        marvelView?.showProgressBar()
        marvelInteractor.getComicDetail(comicId, this)
    }

    override fun onDestroy() {
        marvelInteractor.disposeObservables()
    }



    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ INTERACTOR INERFACE -----------------------------------------------------------
    override fun onComicListReceived(comicList:ArrayList<Comic>) {
        marvelView?.onSuperHeroComicsReceived(comicList)
    }

    override fun onComicListError(error:String) {
//        marvelView?.onSuperHeroComicsError(error)
    }

    override fun onComicDetailsReceived(comic:Comic) {
        marvelView?.onComicDetailReceived(comic)
    }

    override fun onComicDetailError(error:String) {
        marvelView?.onComicDetailError(error)
    }



    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ RESULT HELPERS ----------------------------------------------------------------
    private fun discardNonValidComics(comics: List<Comic>) =
        comics.filter {
            !it.title.isEmpty()
        }

}