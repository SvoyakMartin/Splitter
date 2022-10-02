package ru.svoyakmartin.splitter.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

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

): Serializable