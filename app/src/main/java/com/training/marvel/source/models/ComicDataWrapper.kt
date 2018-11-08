package com.training.marvel.source.models

import arrow.core.Option

/**
 * Created by victor on 21/11/17.
 */
data class ComicDataWrapper(val code:Int,
                            val status:String,
                            val copyright:String,
                            val attributionText:String,
                            val attributionHTML:String,
                            val data:Option<ComicDataContainer>,
                            val etag:String)