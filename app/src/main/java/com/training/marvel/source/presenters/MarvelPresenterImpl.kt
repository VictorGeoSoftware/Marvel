package com.training.marvel.source.presenters

import android.content.Context
import arrow.core.Either
import arrow.effects.IO
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic

/**
 * Created by victor on 14/11/17.
 *
 */

class MarvelPresenterImpl(context: Context) :MarvelPresenter, MarvelInteractor.RequestListener {
    var marvelView: MarvelView? = null
    var marvelInteractor:MarvelInteractor? = null

    init {
        this.marvelInteractor = MarvelInteractorImpl(context)
    }


    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ PRESENTER INERFACE ------------------------------------------------------------
    override fun setView(marvelView: MarvelView) {
        this.marvelView = marvelView
    }

    override fun getSuperHeroComics(): IO<Either<CharacterError, List<Comic>>> =
        marvelInteractor!!.getSuperHeroComics().map { it ->
            it.map { discardNonValidComics(it) } }





    //----- First approach
//    override fun getSuperHeroComics(): Either<CharacterError, ArrayList<Comic>> {
//        marvelView?.showProgressBar()
//
//        return marvelInteractor!!.getSuperHeroComics().fold(
//                {
//                    Log.i(this.javaClass.name, "getSuperHeroComics - error :: $it")
//                    Left(it)
//                },
//                {
//                    Log.i(this.javaClass.name, "getSuperHeroComics - ok! :: $it")
//                    Right(it) // we can set filter logic, etc
//                })
//    }

    override fun getComicDetail(comicId: Long) {
        marvelView?.showProgressBar()
        marvelInteractor?.getComicDetail(comicId, this)
    }

    override fun onDestroy() {
        marvelInteractor?.disposeObservables()
    }



    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ INTERACTOR INERFACE -----------------------------------------------------------
    override fun onComicListReceived(comicList:ArrayList<Comic>) {
        marvelView?.onSuperHeroComicsReceived(comicList)
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



    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ RESULT HELPERS ----------------------------------------------------------------
    private fun discardNonValidComics(comics: ArrayList<Comic>) =
        comics.filter {
            !it.title.isEmpty()
        }

}