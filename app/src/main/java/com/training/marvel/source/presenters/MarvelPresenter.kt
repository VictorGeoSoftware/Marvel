package com.training.marvel.source.presenters

import arrow.data.Reader
import com.training.marvel.source.context.ComicsContext

/**
 * Created by victor on 14/11/17.
 */
interface MarvelPresenter {
    fun setView(marvelView: MarvelView)
    fun getSuperHeroComics(): Reader<ComicsContext.GetComicContext, Unit>
    fun getComicDetail(comicId:Long): Reader<ComicsContext.GetComicContext, Unit>
}