package com.training.marvel.source.presenters

import android.content.Context
import android.util.Log
import arrow.Kind
import arrow.core.Either
import arrow.core.Try
import arrow.core.left
import arrow.core.right
import arrow.effects.IO
import arrow.effects.async
import arrow.effects.fix
import arrow.effects.typeclasses.Async
import com.training.marvel.source.BuildConfig
import com.training.marvel.source.ParentApplication
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic
import com.training.marvel.source.network.MarvelRequest
import com.training.marvel.source.utils.MyUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by victor on 14/11/17.
 *
 */

class MarvelInteractorImpl(context: Context) : MarvelInteractor {
    @Inject lateinit var marvelRequest: MarvelRequest
    var disposableComicList: Disposable? = null
    var disposableComicDetail:Disposable? = null


    init {
        (context as ParentApplication).component.inject(this)
    }

    override fun getSuperHeroComics(): IO<Either<CharacterError, ArrayList<Comic>>> =
        runInAsyncContext(
                f = {
                    val rightNow = Date()
                    val dateFormat = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
                    val requestId:String = dateFormat.format(rightNow)

                    val params:HashMap<String, String> = HashMap()
                    params["ts"] = requestId
                    params["apikey"] = BuildConfig.MARVEL_PUBLIC_KEY
                    params["hash"] = MyUtils.getRequestHash(requestId)


                    // 1º.- Instantiate Retrofit call
                    val call = marvelRequest.getCharacterComics(params)

                    // 2º.- Execute call command
                    call.execute()
                },
                onError = {
                    Log.i("ValeetTest", "MarvelInteractorImpl - onError :: $it")
//                    it.t.left()
                    CharacterError.UnknownServerError.left()
                },
                onSuccess = {
                    if (it.isSuccessful) {
                        val comicList = it.body()?.data?.results!!
                        Log.i("ValeetTest", "MarvelInteractorImpl - onSuccess - comicList :: $comicList")
                        comicList.right()
                    } else {
                        Log.i("ValeetTest", "MarvelInteractorImpl - onSuccess - errorResponse :: ${it.errorBody()}")
                        CharacterError.UnknownServerError.left()
                    }
                },
                AC = IO.async()).fix()

    // ----- first approach
//    override fun getSuperHeroComics(): Either<CharacterError, ArrayList<Comic>> =
//            try {
//                val rightNow = Date()
//                val dateFormat = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
//                val requestId:String = dateFormat.format(rightNow)
//
//                val params:HashMap<String, String> = HashMap()
//                params["ts"] = requestId
//                params["apikey"] = BuildConfig.MARVEL_PUBLIC_KEY
//                params["hash"] = MyUtils.getRequestHash(requestId)
//
//
//                // 1º.- Instantiate Retrofit call
//                val call = marvelRequest.getCharacterComics(params)
//
//                // 2º.- Execute call command
//                val comicDataWrapper = call.execute()
//
//                // 3º.- Handle response
//                // TODO :: should be necessary to unparse errors!
//                if (comicDataWrapper.isSuccessful) {
//                    val comicList = comicDataWrapper.body()?.data?.results!!
//                    Log.i("ValeetTest", "MarvelInteractorImpl - comicList :: $comicList")
//                    Either.Right(comicList)
//                } else{
//                    Either.Left(CharacterError.NoResultError)
//                }
//            } catch (e: HttpException) {
//                Either.Left(CharacterError.NotFoundError)
//            } catch (e: Exception) {
//                Log.i("ValeetTest", "MarvelInteractorImpl - exception :: $e")
//                Either.Left(CharacterError.UnknownServerError)
//            }

    override fun getComicDetail(comicId:Long, requestListener: MarvelInteractor.RequestListener) {
        val rightNow = Date()
        val dateFormat = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
        val requestId:String = dateFormat.format(rightNow)

        val params:HashMap<String, String> = HashMap()
        params.put("ts", requestId)
        params.put("apikey", BuildConfig.MARVEL_PUBLIC_KEY)
        params.put("hash", MyUtils.getRequestHash(requestId))
        Log.i("ValeetTest", "MarvelInteractorImpl - params :: $params")

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



    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ HELPER FUNCTIONS --------------------------------------------------------------
    private fun <F, A, B> runInAsyncContext(f: () -> A, onError: (Throwable) -> B, onSuccess: (A) -> B, AC: Async<F>): Kind<F, B> {
        return AC.async {
            async(CommonPool) {
                val result = Try { f() }.fold(onError, onSuccess)
                it(result.right())
            }
        }
    }
}