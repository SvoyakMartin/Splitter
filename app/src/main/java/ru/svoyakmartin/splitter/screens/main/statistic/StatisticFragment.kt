package ru.svoyakmartin.splitter.screens.main.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.svoyakmartin.splitter.WedgesApplication
import ru.svoyakmartin.splitter.databinding.FragmentStatisticBinding
import ru.svoyakmartin.splitter.util.util

class StatisticFragment : Fragment() {

    private lateinit var binding: FragmentStatisticBinding
    private val viewModel: StatisticViewModel by viewModels {
        StatisticViewModelFactory((requireActivity().application as WedgesApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel.apply {
            with(binding) {
                sumWedges.observe(requireActivity()) {
                    allWedgesSumValue.text = util.num2String(it?:0.0)
                    setCurrentWedgeSum()
                }
                sumInvests.observe(requireActivity()) {
                    investsSumValue.text = util.num2String(it?:0.0)
                    setCurrentWedgeSum()
                }
            }
            // TODO: вывести количество дней
        }
    }

    private fun setCurrentWedgeSum() {
        binding.currentWedgesSumValue.text = with(viewModel){
            util.num2String(((sumWedges.value?:0.0).minus(sumInvests.value?:0.0)))
        }
    }

    companion object {
        @JvmStatic
        fun getInstance() = StatisticFragment()
    }
}