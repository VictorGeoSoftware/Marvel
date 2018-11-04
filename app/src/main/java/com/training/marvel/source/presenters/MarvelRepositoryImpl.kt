package com.training.marvel.source.presenters

import arrow.Kind
import arrow.core.Either
import arrow.core.Try
import arrow.core.left
import arrow.core.right
import arrow.data.Reader
import arrow.data.ReaderApi
import arrow.data.map
import arrow.effects.IO
import arrow.effects.fix
import arrow.effects.typeclasses.Async
import com.training.marvel.source.BuildConfig
import com.training.marvel.source.context.ComicsContext
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic
import com.training.marvel.source.utils.MyUtils
import com.training.marvel.source.utils.trace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.text.SimpleDateFormat
import java.util.*

class MarvelRepositoryImpl: MarvelRepository {


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
                                val comicList = it.body()?.data?.results!!
                                comicList.toList().right()
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