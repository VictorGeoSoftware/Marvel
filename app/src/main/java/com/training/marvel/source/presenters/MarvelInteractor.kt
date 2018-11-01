package com.training.marvel.source.presenters

import arrow.core.Either
import arrow.effects.IO
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic

/**
 * Created by victor on 14/11/17.
 *
 */

interface MarvelInteractor {
    // ----- first approach
    fun getSuperHeroComics(): IO<Either<CharacterError, ArrayList<Comic>>>
    fun getComicDetail(comicId:Long, requestListener: RequestListener)
    fun disposeObservables()

    interface RequestListener {
        fun onComicListReceived(comicList:ArrayList<Comic>)
        fun onComicListError(error:String)
        fun onComicDetailsReceived(comic:Comic)
        fun onComicDetailError(error:String)
    }
}