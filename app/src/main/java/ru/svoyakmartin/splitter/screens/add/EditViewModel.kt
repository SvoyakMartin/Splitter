package ru.svoyakmartin.splitter.screens.add

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.svoyakmartin.splitter.data.WedgeRepository
import ru.svoyakmartin.splitter.model.Total
import ru.svoyakmartin.splitter.model.Wedge
import java.util.*

class EditViewModel(private val repository: WedgeRepository) : ViewModel() {
    lateinit var wedge: Wedge
    val calendar = getCalendarInstance()

    private val _currentDate = MutableLiveData<Long>()
    val currentDate: LiveData<String> = Transformations.map(_currentDate) {
        wedge.getFormattedDate()
    }

    private val _currentSum = MutableLiveData<Double>()
    val currentSum: LiveData<Double>
        get() = _currentSum

    fun initData(_wedge: Wedge) {
        wedge = _wedge

        if (wedge.date > 0) {
            calendar.timeInMillis = wedge.date
        }

        refreshSum()
        setCurrentDate()
    }

    private fun getCalendarInstance(): Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    fun setDate(year: Int, month: Int, day: Int) {
        calendar.set(year, month, day, 0, 0, 0)
        setCurrentDate()
    }

    private fun setCurrentDate() {
        with(wedge) {
            date = calendar.timeInMillis
            _currentDate.value = date
        }
    }

    fun saveData() {
        with(wedge) {
            insertWedge(wedge)
            insertTotal(Total(date, sum, invest))
        }
    }

    private fun insertWedge(wedge: Wedge) = viewModelScope.launch {
        repository.insertWedge(wedge)
    }

    private fun insertTotal(total: Total) = viewModelScope.launch {
        repository.insertTotal(total)
    }

    fun refreshSum() {
        with(wedge) {
            sum = (add + out) / 10 + addExtra
            _currentSum.value = sum
        }
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