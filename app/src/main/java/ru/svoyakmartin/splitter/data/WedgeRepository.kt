package ru.svoyakmartin.splitter.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import ru.svoyakmartin.splitter.model.Total
import ru.svoyakmartin.splitter.model.Wedge

class WedgeRepository(private val wedgeDao: WedgeDao) {
    val allWedge: Flow<List<Wedge>> = wedgeDao.getAllWedges()
    val sumInvests: Flow<Double> = wedgeDao.getSumInvests()
    val sumWedges: Flow<Double> = wedgeDao.getSumWedges()
    val totalDays: Flow<Int> = wedgeDao.getTotalDays()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertWedge(wedge: Wedge) {
        wedgeDao.insertWedge(wedge)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteWedge(wedge: Wedge) {
        wedgeDao.deleteWedge(wedge)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertTotal(total: Total) {
        wedgeDao.insertTotal(total)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteTotal(total: Total) {
        wedgeDao.deleteTotal(total)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getSumWedgesOnDate(date: Long): Flow<Double> {
       return wedgeDao.getSumWedgesOnDate(date)
    }
}