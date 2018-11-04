package com.training.marvel.source.presenters

import arrow.core.Either
import arrow.data.Reader
import arrow.effects.IO
import com.training.marvel.source.context.ComicsContext
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic

/**
 * Created by victor on 14/11/17.
 *
 */

interface MarvelInteractor {
    fun getSuperHeroComics(): Reader<ComicsContext.GetComicContext, IO<Either<CharacterError, List<Comic>>>>
    fun getComicDetail(comicId:Long): Reader<ComicsContext.GetComicContext, IO<Either<CharacterError, Comic>>>

    interface RequestListener {
        fun onComicListReceived(comicList:ArrayList<Comic>)
        fun onComicListError(error: CharacterError)
        fun onComicDetailsReceived(comic:Comic)
        fun onComicDetailError(error: CharacterError)
    }
}