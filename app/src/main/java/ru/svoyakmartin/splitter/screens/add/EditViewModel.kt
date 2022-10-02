package ru.svoyakmartin.splitter.screens.add

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.svoyakmartin.splitter.data.WedgeRepository
import ru.svoyakmartin.splitter.model.Total
import ru.svoyakmartin.splitter.model.Wedge

class EditViewModel(private val repository: WedgeRepository) : ViewModel() {
        fun insertWedge(wedge: Wedge) = viewModelScope.launch {
            repository.insertWedge(wedge)
        }

        fun insertTotal(total: Total) = viewModelScope.launch {
            repository.insertTotal(total)
        }
    }

    class EditViewModelFactory(private val repository: WedgeRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EditViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

}