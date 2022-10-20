package ru.svoyakmartin.splitter

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import ru.svoyakmartin.splitter.model.Wedge
import ru.svoyakmartin.splitter.screens.main.MainActivity
import ru.svoyakmartin.splitter.screens.main.list.WedgeAdapter
import ru.svoyakmartin.splitter.screens.main.list.WedgeListFragment

class WedgeAdapterTests {
    private val context = mock(MainActivity::class.java)

    @Test
    fun adapterSize() {
        val adapter = WedgeAdapter(context as WedgeListFragment)
        val list = listOf(Wedge(0), Wedge(1), Wedge(2))
        adapter.setList(list)
        assertEquals(adapter.itemCount, list.size)
    }
}