package ru.svoyakmartin.splitter.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.svoyakmartin.splitter.util.Util
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "wedges_table")
data class Wedge(

    @PrimaryKey
    @ColumnInfo(name = "date")
    var date: Long = 0,

    @ColumnInfo(name = "add")
    var add: Double = 0.0,

    @ColumnInfo(name = "out")
    var out: Double = 0.0,

    @ColumnInfo(name = "addExtra")
    var addExtra: Double = 0.0,

    @ColumnInfo(name = "invest")
    var invest: Double = 0.0,

    @ColumnInfo(name = "sum")
    var sum: Double = 0.0

): Serializable{
    companion object{
        val DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    }

    fun getFormattedDate(): String {
        return DATE_FORMAT.format(this.date)
    }
}