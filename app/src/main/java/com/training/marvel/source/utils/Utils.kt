package com.training.marvel.source.utils

fun Any.trace(traceToShow: String) {
    System.out.println("victor | ${Any::class.java.name} -> $traceToShow")
}