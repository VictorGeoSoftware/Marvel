package com.training.marvel.source

import com.training.marvel.source.utils.MyUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testApiKey() {
        val rightNow = Date()
        val dateFormat = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
        val requestId:String = dateFormat.format(rightNow)
        val hash:String = MyUtils.getRequestHash(requestId)

        assertNotNull(hash)
    }
}
