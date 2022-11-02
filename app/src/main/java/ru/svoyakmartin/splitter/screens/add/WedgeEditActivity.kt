package ru.svoyakmartin.splitter.screens.add

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import ru.svoyakmartin.splitter.R
import ru.svoyakmartin.splitter.WedgesApplication
import ru.svoyakmartin.splitter.databinding.ActivityWedgeEditBinding
import ru.svoyakmartin.splitter.model.Wedge
import java.util.*

class WedgeEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWedgeEditBinding
    private val viewModel: EditViewModel by viewModels {
        EditViewModelFactory((this.application as WedgesApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityWedgeEditBinding>(
            this,
            R.layout.activity_wedge_edit
        ).apply {
            editViewModel = viewModel
            lifecycleOwner = this@WedgeEditActivity
            wedgeEditActivity = this@WedgeEditActivity
        }

        init()
    }

    fun done() {
        viewModel.saveData()

        finish()
    }

    private fun init() {
        val wedge = if (Build.VERSION.SDK_INT >= 33) {
            intent.getSerializableExtra("wedge", Wedge::class.java)
        } else {
            intent.getSerializableExtra("wedge")
        } as Wedge

        viewModel.initData(wedge)

        // TODO: работа с полями:
        // TODO: ► 2 знака после запятой на вью всегда
        // TODO: ► два знака после запятой при ввооде
        // TODO: ► иконка инфо в конце поля ввода с объяснением поля
    }

    fun changeDate() {
        with(viewModel.calendar) {
            val year = get(Calendar.YEAR)
            val month = get(Calendar.MONTH)
            val day = get(Calendar.DAY_OF_MONTH)

            val listener = DatePickerDialog.OnDateSetListener { _, newYear, newMonth, newDay ->
                viewModel.setDate(newYear, newMonth, newDay)
            }

            DatePickerDialog(this@WedgeEditActivity, listener, year, month, day).show()
        }
    }
}