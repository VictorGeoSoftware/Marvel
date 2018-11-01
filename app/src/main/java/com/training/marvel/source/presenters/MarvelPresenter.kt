package com.training.marvel.source.presenters

import arrow.core.Either
import arrow.effects.IO
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic

/**
 * Created by victor on 14/11/17.
 */
interface MarvelPresenter {
    fun setView(marvelView: MarvelView)
    fun getSuperHeroComics(): IO<Either<CharacterError, List<Comic>>>
    // ----- First approach
//    fun getSuperHeroComics(): Either<CharacterError, ArrayList<Comic>>
    fun getComicDetail(comicId:Long)
    fun onDestroy()
}