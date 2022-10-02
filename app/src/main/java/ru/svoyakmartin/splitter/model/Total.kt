package ru.svoyakmartin.splitter.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "totals_table")
data class Total (
    @PrimaryKey
    @ColumnInfo(name = "date")
    var date: Long = 0,

    @ColumnInfo(name = "wedge")
    var wedge: Double = 0.0,

    @ColumnInfo(name = "invest")
    var invest: Double = 0.0

): Serializable
