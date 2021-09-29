package com.acompworld.teamconnect

import com.acompworld.teamconnect.api.service.MyClient
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

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
    fun `Am I getting My Wall`(){
        runBlocking{
            val api = MyClient.api
            val response = api.getMyWall("wrihq")
            assertNotNull(response.body())
        }
    }

    @Test
    fun `directory aagai`(){
        runBlocking {
            val api = MyClient.api
            val response = api.getDirectory("wrihq")
            assertNotNull(response.body())
        }
    }

    //
    @Test
    fun getEmployeeByID(){
        runBlocking {
            val api = MyClient.api
            val response = api.getEmployeeById("wrihq","087277",)
            assertNotNull(response.body())
        }
    }

    @Test
    fun getContactByID(){
        runBlocking {
            val api = MyClient.api
            val response = api.getContactById("wrihq","2",)
            assertNotNull(response.body())
        }
    }

}