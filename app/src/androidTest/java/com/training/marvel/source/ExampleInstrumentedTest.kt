package com.training.marvel.source

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.training.marvel.source.utils.MyUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {



    @Before
    fun initParams() {

    }

    @Test
    fun testRequestHash() {
        val result = MyUtils.getRequestHash(Date())
        System.out.println("SE GENERA :: " + result)
        assertNotNull(result)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.test.marvel.vallettest", appContext.packageName)
    }
}
