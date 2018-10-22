package com.training.marvel.source.network

import com.training.marvel.source.BuildConfig
import com.training.marvel.source.models.ComicDataWrapper
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import java.util.*

/**
 * Created by victor on 13/11/17.
 *
 */

interface MarvelRequest {
    @Headers("Content-Type: application/json;charset=UTF-8")

    @GET("/v1/public/characters/" + BuildConfig.MARVEL_CHARACTER_ID + "/comics")
    //fun getCharacterComics(@QueryMap params:HashMap<String, String>): Observable<Response<ComicDataWrapper>>
    fun getCharacterComics(@QueryMap params:HashMap<String, String>): Observable<ComicDataWrapper>

    @GET("/v1/public/comics/{comicId}")
    fun getComicDetail(@Path("comicId") comicId:Long, @QueryMap params:HashMap<String, String>): Observable<Response<ComicDataWrapper>>
}