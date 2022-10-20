package ru.svoyakmartin.splitter

import org.junit.Test

import org.junit.Assert.*
import ru.svoyakmartin.splitter.util.Util

class ExampleUnitTest {
    @Test
    fun testNum2String() {
        assertTrue("test num2String",
            Util.num2String(2.0) == "2.00"
                    && Util.num2String(2.499) == "2.50"
                    && Util.num2String(2.3) == "2.30")
    }
}