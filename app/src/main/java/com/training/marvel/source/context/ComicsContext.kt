package com.training.marvel.source.context

import android.content.Context
import arrow.effects.IO
import arrow.effects.async
import com.training.marvel.source.network.MarvelRequest
import com.training.marvel.source.presenters.MarvelView

sealed class ComicsContext {
    abstract val context: Context
    abstract val view: MarvelView
    abstract val marvelRequest: MarvelRequest

    val threading = IO.async()

    data class GetComicContext(override val context: Context,
                               override val view: MarvelView,
                               override val marvelRequest: MarvelRequest): ComicsContext()
}