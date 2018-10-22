package com.training.marvel.source.presenters

import android.content.Context
import android.util.Log
import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.training.marvel.source.BuildConfig
import com.training.marvel.source.ParentApplication
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic
import com.training.marvel.source.network.MarvelRequest
import com.training.marvel.source.utils.MyUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.Exception
import kotlin.collections.ArrayList

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


    /*
    fun getAllHeroes(service: HeroesService): Either<CharacterError, List<SuperHero>> =
    try {
      Right(service.getCharacters().map { SuperHero(it.id, it.name, it.thumbnailUrl, it.description) })
    } catch (e: MarvelAuthApiException) {
      Left(AuthenticationError)
    } catch (e: MarvelApiException) {
      if (e.httpCode == HttpURLConnection.HTTP_NOT_FOUND) {
        Left(NotFoundError)
      } else {
        Left(UnknownServerError)
      }
    }
     */
    override fun getSuperHeroComics(requestListener: MarvelInteractor.RequestListener): Either<CharacterError, ArrayList<Comic>> =
            try {
                val rightNow = Date()
                val dateFormat = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
                val requestId:String = dateFormat.format(rightNow)


                // TODO :: download example project and understand web service
                // https://github.com/JorgeCastilloPrz/ArrowAndroidSamples/blob/master/monad-stack/src/main/java/com/github/jorgecastillo/kotlinandroid/data/datasource/remote/MarvelNetworkDataSource.kt
                val params:HashMap<String, String> = HashMap()
                params.put("ts", requestId)
                params.put("apikey", BuildConfig.MARVEL_PUBLIC_KEY)
                params.put("hash", MyUtils.getRequestHash(requestId))
                Log.i("ValeetTest", "MarvelInteractorImpl - params :: $params")
                Right(marvelRequest.getCharacterComics().subscribe { it.data.results })
            } catch (e: HttpException) {
                Left(CharacterError.NotFoundError)
            } catch (e: Exception) {
                Left(CharacterError.UnknownServerError)
            }



    fun getSuperHeroComics_old(requestListener: MarvelInteractor.RequestListener) {
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