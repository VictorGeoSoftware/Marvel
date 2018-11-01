package com.training.marvel.source.models

sealed class CharacterError {
    object AuthenticationError: CharacterError()
    object NoResultError: CharacterError()
    object NotFoundError: CharacterError()
    object UnknownServerError: CharacterError()
}