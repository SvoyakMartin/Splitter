package ru.svoyakmartin.splitter.screens.main.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import ru.svoyakmartin.splitter.R
import ru.svoyakmartin.splitter.WedgesApplication
import ru.svoyakmartin.splitter.databinding.FragmentStatisticBinding

class StatisticFragment : Fragment() {
    private lateinit var binding: FragmentStatisticBinding
    private val viewModel: StatisticViewModel by viewModels {
        StatisticViewModelFactory((requireActivity().application as WedgesApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistic, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            statisticViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }
}