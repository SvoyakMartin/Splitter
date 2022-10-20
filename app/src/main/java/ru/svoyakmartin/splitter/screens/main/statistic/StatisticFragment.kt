package ru.svoyakmartin.splitter.screens.main.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.svoyakmartin.splitter.WedgesApplication
import ru.svoyakmartin.splitter.databinding.FragmentStatisticBinding
import ru.svoyakmartin.splitter.util.Util

class StatisticFragment : Fragment() {
    private var _binding: FragmentStatisticBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatisticViewModel by viewModels {
        StatisticViewModelFactory((requireActivity().application as WedgesApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticBinding.inflate(inflater)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel.apply {
            with(binding) {
                sumWedges.observe(requireActivity()) {
                    allWedgesSumValue.text = Util.num2String(it ?: 0.0)
                    setCurrentWedgeSum()
                }
                sumInvests.observe(requireActivity()) {
                    investsSumValue.text = Util.num2String(it ?: 0.0)
                    setCurrentWedgeSum()
                }
                totalDays.observe(requireActivity()) {
                    amountOfDaysValue.text = (it ?: 0).toString()
                }
            }
        }
    }

    private fun setCurrentWedgeSum() {
        binding.currentWedgesSumValue.text = with(viewModel) {
            Util.num2String(((sumWedges.value ?: 0.0).minus(sumInvests.value ?: 0.0)))
        }
    }
}

// TODO: добавить количество дней без перерыва и максимальное без перерыва