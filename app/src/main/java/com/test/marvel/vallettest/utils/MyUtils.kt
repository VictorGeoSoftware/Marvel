package com.test.marvel.vallettest.utils

import android.content.Context
import android.util.TypedValue
import com.test.marvel.vallettest.BuildConfig

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