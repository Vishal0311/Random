package com.acompworld.teamconnect

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.acompworld.teamconnect.api.service.MyClient
import com.acompworld.teamconnect.repo.TeamConnectRepository
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.acompworld.teamconnect", appContext.packageName)
    }

//    @Test
//    fun mywall(){
//        val api = MyClient.api
//        runBlocking {
//            val res = api.getAboutUs("wrihq")
//            assertNotNull(res.body())
//        }
//    }

}