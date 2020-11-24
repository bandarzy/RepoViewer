package com.example.githubviewer.util

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class QueryTransformerKtTest (
    private val stringToFormat: String,
    private val expectedResult: String) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() : Collection<Array<Any>> {
            return listOf(
                arrayOf("element1, element2", "element1+element2"),
                arrayOf("element1,     element2", "element1+element2"),
                arrayOf("", "")
            )
        }
    }

    @Test
    fun testQueryFormat() {
        assertEquals(expectedResult, formatQuery(stringToFormat));
    }

}