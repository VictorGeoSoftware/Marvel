package com.training.marvel.source.presenters

import arrow.Kind
import arrow.core.*
import arrow.data.*
import arrow.effects.*
import arrow.effects.instances.io.async.async
import arrow.effects.instances.io.monad.monad
import arrow.effects.observablek.monad.monad
import arrow.effects.typeclasses.Async
import arrow.instances.either.monad.monad
import arrow.instances.monad
import arrow.typeclasses.binding
import com.training.marvel.source.BuildConfig
import com.training.marvel.source.context.ComicsContext
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic
import com.training.marvel.source.models.ComicDataWrapper
import com.training.marvel.source.models.NoResultError
import com.training.marvel.source.utils.MyUtils
import com.training.marvel.source.utils.trace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class MarvelRepositoryImpl: MarvelRepository {


    fun getSuperHeroComicsExample(maybeResponse: Either<CharacterError, ComicDataWrapper>): Either<CharacterError, List<Comic>> {
        return Either.monad<CharacterError>().binding {
            val wrapper = maybeResponse.bind()
            val comicDataContainer = wrapper.data.toEither {
                CharacterError.NoResultError
            }.bind()
            comicDataContainer.results
        }.fix()
    }

//    fun getSuperHeroComicsT(response: ComicDataWrapper): ObservableK<Either<CharacterError, List<Comic>>> {
//
//        return EitherT.monad<ForObservableK, CharacterError>(ObservableK.monad()).binding{
//            val data = EitherT(response.data.toEither { NoResultError }).bind()
//        }.value()
//    }

    fun getSuperHeroComicsTObservableK(response: ComicDataWrapper): ObservableK<Either<CharacterError, List<Comic>>> {
        return EitherT.monad<ForObservableK, CharacterError>(ObservableK.monad()).binding {
            val data = EitherT(ObservableK.just(response.data.toEither { CharacterError.NotFoundError })).bind()
            data.results
        }.value().fix()
    }

    fun getSuperHeroComicsTIO(response: ComicDataWrapper): IO<Either<CharacterError, List<Comic>>> {
        EitherT.monad<ForIO, CharacterError>(IO.monad()).binding {
            val data = EitherT(IO.async { response.data.fold( { Left(CharacterError.NotFoundError) }, { Right(response.data) }) }).bind()
            data.results
        }.value().fix()
    }


    override fun getSuperHeroComics(): Reader<ComicsContext.GetComicContext, IO<Either<CharacterError, List<Comic>>>> =
            ReaderApi.ask<ComicsContext.GetComicContext>().map { ctx ->
                runInAsyncContext(
                        f = {
                            val rightNow = Date()
                            val dateFormat = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
                            val requestId:String = dateFormat.format(rightNow)

                            val params: HashMap<String, String> = HashMap()
                            params["ts"] = requestId
                            params["apikey"] = BuildConfig.MARVEL_PUBLIC_KEY
                            params["hash"] = MyUtils.getRequestHash(requestId)

                            val call = ctx.marvelRequest.getCharacterComics(params)
                            call.execute()
                        },
                        onError = {
                            CharacterError.UnknownServerError.left()
                        },
                        onSuccess = {
                            if (it.isSuccessful) {
                                // todo acabar lo de arriba y luego implementar aquí
                                val comicDataContainer = it.body()!!.data.toEither {

                                }.map {
                                    it.results
                                }

                            } else {
                                CharacterError.UnknownServerError.left()
                            }
                        },
                        AC = ctx.threading).fix()}

    override fun getComicDetail(comicId: Long): Reader<ComicsContext.GetComicContext, IO<Either<CharacterError, Comic>>> =
            ReaderApi.ask<ComicsContext.GetComicContext>().map { context ->
                runInAsyncContext(
                        f = {
                            val rightNow = Date()
                            val dateFormat = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
                            val requestId:String = dateFormat.format(rightNow)

                            val params:HashMap<String, String> = HashMap()
                            params.put("ts", requestId)
                            params.put("apikey", BuildConfig.MARVEL_PUBLIC_KEY)
                            params.put("hash", MyUtils.getRequestHash(requestId))
                            trace("getComicDetail - params :: $params")

                            context.marvelRequest.getComicDetail(comicId, params).execute()
                        },
                        onError = {
                            CharacterError.UnknownServerError.left()
                        },
                        onSuccess = {
                            if (it.isSuccessful) {
                                trace("getComicDetail - response successfull :: $it")

                                if (it.body()?.data?.results!!.isNotEmpty()) {
                                    it.body()?.data?.results!![0].right()
                                } else {
                                    CharacterError.NoResultError.left()
                                }

                            } else {
                                trace("getComicDetail - response failed :: $it")
                                CharacterError.NoResultError.left()
                            }
                        },
                        AC = context.threading).fix()
            }



    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ HELPER FUNCTIONS --------------------------------------------------------------
    private fun <F, A, B> runInAsyncContext(f: () -> A, onError: (Throwable) -> B, onSuccess: (A) -> B, AC: Async<F>): Kind<F, B> {
        return AC.async {
            CoroutineScope(Dispatchers.Default).async {
                val result = Try { f() }.fold(onError, onSuccess)
                it(result.right())
            }
        }
    }
}