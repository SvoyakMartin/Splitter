package ru.svoyakmartin.splitter.screens.main.list

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.svoyakmartin.splitter.model.Wedge
import ru.svoyakmartin.splitter.data.WedgeRepository
import ru.svoyakmartin.splitter.model.Total

class WedgeViewModel(private val repository: WedgeRepository) : ViewModel() {
    val allWedges: LiveData<List<Wedge>> = repository.allWedge.asLiveData()

    fun deleteWedge(wedge: Wedge) = viewModelScope.launch {
        repository.deleteWedge(wedge)
    }

    fun deleteTotal(total: Total) = viewModelScope.launch {
        repository.deleteTotal(total)
    }

    fun getSumWedgesOnDate(date: Long): LiveData<Double> {
        return  repository.getSumWedgesOnDate(date).asLiveData()
    }
}

class WedgeViewModelFactory(private val repository: WedgeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WedgeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WedgeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}