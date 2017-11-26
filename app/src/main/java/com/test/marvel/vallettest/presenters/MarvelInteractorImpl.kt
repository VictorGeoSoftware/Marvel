package com.test.marvel.vallettest.presenters

import android.content.Context
import android.util.Log
import com.test.marvel.vallettest.BuildConfig
import com.test.marvel.vallettest.ParentApplication
import com.test.marvel.vallettest.network.MarvelRequest
import com.test.marvel.vallettest.utils.MyUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by victor on 14/11/17.
 *
 */

class MarvelInteractorImpl: MarvelInteractor {
    @Inject lateinit var marvelRequest: MarvelRequest
    var disposableComicList: Disposable? = null
    var disposableComicDetail:Disposable? = null


    constructor(context:Context) {
        (context as ParentApplication).component.inject(this)
    }


    override fun getSuperHeroComics(requestListener: MarvelInteractor.RequestListener) {
        val rightNow = Date()
        val dateFormat = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
        val requestId:String = dateFormat.format(rightNow)

        val params:HashMap<String, String> = HashMap()
        params.put("ts", requestId)
        params.put("apikey", BuildConfig.MARVEL_PUBLIC_KEY)
        params.put("hash", MyUtils.getRequestHash(requestId))
        Log.i("ValeetTest", "MarvelInteractorImpl - params :: " + params)

        disposableComicList = marvelRequest.getCharacterComics(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            response ->
                            Log.i("ValeetApp", "MarvelInteractorImpl - url :: " + response.raw().request().url())
                            Log.i("ValeetApp", "MarvelInteractorImpl - response :: " + response.body())
                            requestListener.onComicListReceived(response.body()?.data?.results)
                        },
                        {
                            e ->
                            requestListener.onComicListError(e.localizedMessage)
                            Log.i("ValeetApp", "MarvelInteractorImpl - response :: " + e.localizedMessage)
                        }
                )


    }

    override fun getComicDetail(comicId:Long, requestListener: MarvelInteractor.RequestListener) {
        val rightNow = Date()
        val dateFormat = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
        val requestId:String = dateFormat.format(rightNow)

        val params:HashMap<String, String> = HashMap()
        params.put("ts", requestId)
        params.put("apikey", BuildConfig.MARVEL_PUBLIC_KEY)
        params.put("hash", MyUtils.getRequestHash(requestId))
        Log.i("ValeetTest", "MarvelInteractorImpl - params :: " + params)

        disposableComicDetail = marvelRequest.getComicDetail(comicId, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            response ->
                            Log.i("ValeetApp", "MarvelInteractorImpl - url :: " + response.raw().request().url())
                            Log.i("ValeetApp", "MarvelInteractorImpl - response :: " + response.body())
                            if (response.body()?.data?.results?.get(0) != null) {
                                val receivedComic = response.body()?.data?.results?.get(0)

                                if (receivedComic != null) {
                                    requestListener.onComicDetailsReceived(receivedComic)
                                }
                            }
                        },
                        {
                            e ->
                            Log.i("ValeetApp", "MarvelInteractorImpl - response :: " + e.localizedMessage)
                            requestListener.onComicListError(e.localizedMessage)
                        }
                )
    }

    override fun disposeObservables() {
        disposableComicList?.dispose()
        disposableComicDetail?.dispose()
    }
}