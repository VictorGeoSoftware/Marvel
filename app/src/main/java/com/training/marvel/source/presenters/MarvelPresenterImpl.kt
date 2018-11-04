package com.training.marvel.source.presenters

import android.content.Context
import arrow.data.ReaderApi
import arrow.data.flatMap
import arrow.data.map
import com.training.marvel.source.context.ComicsContext
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic

/**
 * Created by victor on 14/11/17.
 *
 */

class MarvelPresenterImpl : MarvelPresenter {
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

    override fun getComicDetail(comicId: Long) = ReaderApi.ask<ComicsContext.GetComicContext>().flatMap { (_: Context, view: MarvelView) ->
        view.showProgressBar()
        marvelInteractor.getComicDetail(comicId).map {io ->
            io.unsafeRunAsync {
                it.map { maybeComicDetail ->
                    maybeComicDetail.fold(
                            { error -> view.onComicDetailError(error) },
                            { comicDetail -> view.onComicDetailReceived(comicDetail) })
                }
            }
        }
    }
}