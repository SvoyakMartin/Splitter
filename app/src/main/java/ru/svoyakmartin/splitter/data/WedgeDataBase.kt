package ru.svoyakmartin.splitter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.svoyakmartin.splitter.model.Wedge
import ru.svoyakmartin.splitter.model.Total

@Database(entities = [Wedge::class, Total::class], version = 1, exportSchema = false)
abstract class WedgeDataBase : RoomDatabase() {

    abstract fun getWedgeDao(): WedgeDao

    companion object {
        @Volatile
        private var INSTANCE: WedgeDataBase? = null

        fun getDatabase(context: Context): WedgeDataBase {
            return (INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    WedgeDataBase::class.java,
                    "wedges.db"
                ).build()
                INSTANCE
            })!!
        }
    }
}