package ru.svoyakmartin.splitter.screens.main.statistic

import androidx.lifecycle.*
import ru.svoyakmartin.splitter.data.WedgeRepository

class StatisticViewModel(repository: WedgeRepository) : ViewModel() {
    val sumInvests: LiveData<Double> = repository.sumInvests.asLiveData()
    val sumWedges: LiveData<Double> = repository.sumWedges.asLiveData()
    val currentSumWedges: MediatorLiveData<Double> = MediatorLiveData<Double>().apply {
        addSource(sumInvests) { setCurrentSumWedges() }
        addSource(sumWedges) { setCurrentSumWedges() }
    }
    val totalDays: LiveData<Int> = repository.totalDays.asLiveData()

    private fun setCurrentSumWedges() {
        currentSumWedges.value =
            sumWedges.value?.let { w ->
                sumInvests.value?.let { i ->
                    w - i
                }
            }
    }
}

class StatisticViewModelFactory(private val repository: WedgeRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}