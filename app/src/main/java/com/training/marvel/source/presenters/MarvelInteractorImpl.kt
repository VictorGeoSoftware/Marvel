package com.training.marvel.source.presenters

import arrow.core.Either
import arrow.data.Reader
import arrow.data.map
import arrow.effects.IO
import com.training.marvel.source.context.ComicsContext
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic
import com.training.marvel.source.models.ComicDataWrapper

/**
 * Created by victor on 14/11/17.
 *
 */

class MarvelInteractorImpl : MarvelInteractor {

    private var marvelRepository: MarvelRepository = MarvelRepositoryImpl()


    override fun getSuperHeroComics(): Reader<ComicsContext.GetComicContext, IO<Either<CharacterError, List<Comic>>>> =
            marvelRepository.getSuperHeroComics().map { io ->
                io.map { maybeComicList ->
                    maybeComicList.map {
                        discardNonValidComics(it)
                    }
                }
            }

    override fun getComicDetail(comicId:Long): Reader<ComicsContext.GetComicContext, IO<Either<CharacterError, Comic>>> =
            marvelRepository.getComicDetail(comicId).map {io ->
                io.map { maybeComic ->
                    maybeComic.map { it }
                }
            }



    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ RESULT HELPERS ----------------------------------------------------------------
    private fun discardNonValidComics(comics: List<Comic>) =
            comics.filter {
                !it.title.isEmpty()
            }
}