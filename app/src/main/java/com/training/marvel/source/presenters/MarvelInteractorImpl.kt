package com.training.marvel.source.presenters

import android.util.Log
import arrow.core.Either
import arrow.data.Reader
import arrow.data.map
import arrow.effects.IO
import com.training.marvel.source.BuildConfig
import com.training.marvel.source.context.ComicsContext
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic
import com.training.marvel.source.utils.MyUtils
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by victor on 14/11/17.
 *
 */

class MarvelInteractorImpl : MarvelInteractor {

    var marvelRepository: MarvelRepository = MarvelRepositoryImpl()
    var disposableComicList: Disposable? = null
    var disposableComicDetail:Disposable? = null


    override fun getSuperHeroComics(): Reader<ComicsContext.GetComicContext, IO<Either<CharacterError, List<Comic>>>> =
            marvelRepository.getSuperHeroComics().map { io ->
                io.map { maybeHeroes ->
                    maybeHeroes.map {
                        discardNonValidComics(it)
                    }
                }
            }

    override fun getComicDetail(comicId:Long, requestListener: MarvelInteractor.RequestListener) {
        val rightNow = Date()
        val dateFormat = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
        val requestId:String = dateFormat.format(rightNow)

        val params:HashMap<String, String> = HashMap()
        params.put("ts", requestId)
        params.put("apikey", BuildConfig.MARVEL_PUBLIC_KEY)
        params.put("hash", MyUtils.getRequestHash(requestId))
        Log.i("ValeetTest", "MarvelInteractorImpl - params :: $params")

        // todo ::  pasar a FP
//        disposableComicDetail = marvelRequest.getComicDetail(comicId, params)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        {
//                            response ->
//                            Log.i("ValeetApp", "MarvelInteractorImpl - url :: " + response.raw().request().url())
//                            Log.i("ValeetApp", "MarvelInteractorImpl - response :: " + response.body())
//                            if (response.body()?.data?.results?.get(0) != null) {
//                                val receivedComic = response.body()?.data?.results?.get(0)
//
//                                if (receivedComic != null) {
//                                    requestListener.onComicDetailsReceived(receivedComic)
//                                }
//                            }
//                        },
//                        {
//                            e ->
//                            Log.i("ValeetApp", "MarvelInteractorImpl - response :: " + e.localizedMessage)
//                            requestListener.onComicListError(e.localizedMessage)
//                        }
//                )
    }

    override fun disposeObservables() {
        disposableComicList?.dispose()
        disposableComicDetail?.dispose()
    }



    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ RESULT HELPERS ----------------------------------------------------------------
    private fun discardNonValidComics(comics: List<Comic>) =
            comics.filter {
                !it.title.isEmpty()
            }
}