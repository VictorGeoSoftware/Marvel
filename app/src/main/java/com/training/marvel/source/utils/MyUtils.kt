package com.training.marvel.source.utils

import android.content.Context
import android.util.TypedValue
import com.training.marvel.source.BuildConfig

/**
 * Created by victor on 13/11/17.
 *
 */
class MyUtils {
    companion object {
        fun getRequestHash(requestId:String) : String {
            val initialValue:String = requestId + BuildConfig.MARVEL_PRIVATE_KEY + BuildConfig.MARVEL_PUBLIC_KEY
            val valueByteArray:ByteArray = initialValue.toByteArray(Charsets.UTF_8)

            return HASH.md5(valueByteArray)
        }

        fun getDpFromValue(context: Context, value: Int): Int =
                Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), context.resources.displayMetrics))
    }

}