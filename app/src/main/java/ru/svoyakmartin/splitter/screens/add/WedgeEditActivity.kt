package ru.svoyakmartin.splitter.screens.add

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import ru.svoyakmartin.splitter.WedgesApplication
import ru.svoyakmartin.splitter.model.Wedge
import ru.svoyakmartin.splitter.databinding.ActivityWedgeEditBinding
import ru.svoyakmartin.splitter.model.Total
import ru.svoyakmartin.splitter.util.util

class WedgeEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWedgeEditBinding

    @RequiresApi(Build.VERSION_CODES.N)
    private var calendar = getCalendarInstance()
    private lateinit var wedge: Wedge
    private val viewModel: EditViewModel by viewModels {
        EditViewModelFactory((this.application as WedgesApplication).repository)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWedgeEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun init() {
        val hasWedge = intent.hasExtra("wedge")

        wedge = if (hasWedge) {
            if (Build.VERSION.SDK_INT >= 33) {
                intent.getSerializableExtra("wedge", Wedge::class.java)
            } else {
                intent.getSerializableExtra("wedge")
            } as Wedge
        } else Wedge()

        binding.apply {
            doneButton.setOnClickListener {
                wedge.apply {
                    date = calendar.timeInMillis
                    add = getNumber(addEdit)
                    out = getNumber(outEdit)
                    addExtra = getNumber(addExtraEdit)
                    invest = getNumber(investEdit)
                    sum = getNumber(sumValue)
                }

                viewModel.apply {
                    insertWedge(wedge)
                    with(wedge) {
                        insertTotal(Total(date, sum, invest))
                    }
                }
                finish()
            }

            dateEdit.setOnClickListener {
                changeDate()
            }

            if (hasWedge) {
                with(wedge){
                    calendar.timeInMillis = date
                    addEdit.text = getEditable(add)
                    outEdit.text = getEditable(out)
                    addExtraEdit.text = getEditable(addExtra)
                    investEdit.text = getEditable(invest)
                }
                refreshSum()
            }

            displayFormattedDate()

//            val filter = arrayOf<InputFilter>(DecimalDigitsInputFilter(5, 2))

//            addEdit.filters = filter
            addEdit.doAfterTextChanged {
                refreshSum()
            }

//            outEdit.filters = filter
            outEdit.doAfterTextChanged {
                refreshSum()
            }

//            addExtraEdit.filters = filter
            addExtraEdit.doAfterTextChanged {
                refreshSum()
            }

//            investEdit.filters = filter
            // TODO: работа с полями:
            // TODO: ► 2 знака после запятой на вью всегда
            // TODO: ► выделение текста при изменении
            // TODO: ► два знака после запятой при ввооде
        }
    }

    private fun getNumber(view: TextView): Double {
        val text = view.text

        return if (text.isEmpty()
            || text.toString() == "."
        ) {
            0.0
        } else {
            text.toString().toDouble()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCalendarInstance(): Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun changeDate() {
        with(calendar){
            var year = get(Calendar.YEAR)
            var month = get(Calendar.MONTH)
            var day = get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                this@WedgeEditActivity,
                { _, newYear, newMonth, newDay ->//DatePickerDialog.OnDateSetListener { ...
                    year = newYear
                    month = newMonth + 1
                    day = newDay

                    set(newYear, newMonth, newDay, 0, 0, 0)
                    displayFormattedDate()
                },
                year,
                month,
                day
            ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun displayFormattedDate() {
        binding.dateEdit.text = util.getFormattedDate(calendar.timeInMillis)
    }

    private fun getEditable(number: Double): Editable {
        return Editable.Factory.getInstance().newEditable(number.toString())
    }

    private fun refreshSum() {
        with(binding){
            val sum = (getNumber(addEdit) + getNumber(outEdit)) / 10 + getNumber(addExtraEdit)
            sumValue.text = util.num2String(sum)
        }
    }
}