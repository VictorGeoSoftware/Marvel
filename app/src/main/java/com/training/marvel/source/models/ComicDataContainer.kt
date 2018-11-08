package com.training.marvel.source.models

/**
 * Created by victor on 21/11/17.
 */
data class ComicDataContainer(val offset:Int,
                              val limit:Int,
                              val total:Int,
                              val count:Int,
                              val results:List<Comic>)