package com.test.marvel.vallettest.presenters

import com.test.marvel.vallettest.models.Comic

/**
 * Created by victor on 14/11/17.
 *
 */

interface MarvelInteractor {
    fun getSuperHeroComics(requestListener: RequestListener)
    fun getComicDetail(comicId:Long, requestListener: RequestListener)
    fun disposeObservables()

    interface RequestListener {
        fun onComicListReceived(comicList:ArrayList<Comic>?)
        fun onComicListError(error:String)
        fun onComicDetailsReceived(comic:Comic)
        fun onComicDetailError(error:String)
    }
}