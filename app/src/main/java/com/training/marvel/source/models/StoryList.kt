package com.training.marvel.source.models

/**
 * Created by victor on 21/11/17.
 */
data class StoryList(val available:Int,
                val returned:Int,
                val collectionURI:String,
                val items:ArrayList<StorySummary>)