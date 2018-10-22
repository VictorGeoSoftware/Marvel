package com.training.marvel.source.models

/**
 * Created by victor on 21/11/17.
 */
data class ComicDataWrapper(val code:Int,
                            val status:String,
                            val copyright:String,
                            val attributionText:String,
                            val attributionHTML:String,
                            val data:ComicDataContainer,
                            val etag:String)