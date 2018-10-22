package com.training.marvel.source.presenters

import com.training.marvel.source.models.Comic

/**
 * Created by victor on 14/11/17.
 *
 */

interface MarvelView {
    fun showProgressBar()
    fun onSuperHeroComicsReceived(comicList:ArrayList<Comic>)
    fun onSuperHeroComicsError(error:String)
    fun onComicDetailReceived(comic:Comic)
    fun onComicDetailError(error:String)
}