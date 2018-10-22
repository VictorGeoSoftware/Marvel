package com.training.marvel.source.presenters

/**
 * Created by victor on 14/11/17.
 */
interface MarvelPresenter {
    fun setView(marvelView: MarvelView)
    fun getSuperHeroComics()
    fun getComicDetail(comicId:Long)
    fun onDestroy()
}