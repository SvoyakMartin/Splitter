package ru.svoyakmartin.splitter

import android.app.Application
import ru.svoyakmartin.splitter.data.WedgeDataBase
import ru.svoyakmartin.splitter.data.WedgeRepository

class WedgesApplication : Application() {
    private val database by lazy { WedgeDataBase.getDatabase(this) }
    val repository by lazy { WedgeRepository(database.getWedgeDao()) }
}