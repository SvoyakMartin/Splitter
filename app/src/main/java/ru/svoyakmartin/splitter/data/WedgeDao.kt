package ru.svoyakmartin.splitter.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.svoyakmartin.splitter.model.Total
import ru.svoyakmartin.splitter.model.Wedge

@Dao
interface WedgeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWedge(wedge: Wedge)

    @Delete
    suspend fun deleteWedge(wedge: Wedge)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTotal(total: Total)

    @Delete
    suspend fun deleteTotal(total: Total)

    @Query("SELECT * FROM wedges_table ORDER BY date DESC")
    fun getAllWedges(): Flow<List<Wedge>>

    @Query("SELECT SUM(invest) FROM totals_table")
    fun getSumInvests(): Flow<Double>

    @Query("SELECT SUM(wedge) FROM totals_table")
    fun getSumWedges(): Flow<Double>

    @Query("SELECT COUNT(DISTINCT date) FROM totals_table")
    fun getTotalDays(): Flow<Int>

    @Query("SELECT SUM(wedge) - SUM(invest) AS sum FROM totals_table WHERE date <= :date")
    fun getSumWedgesOnDate(date: Long): Flow<Double>


}
