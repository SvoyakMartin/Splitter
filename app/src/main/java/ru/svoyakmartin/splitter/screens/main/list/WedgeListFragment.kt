package ru.svoyakmartin.splitter.screens.main.list

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.svoyakmartin.splitter.*
import ru.svoyakmartin.splitter.databinding.FragmentWedgeListBinding
import ru.svoyakmartin.splitter.model.Total
import ru.svoyakmartin.splitter.model.Wedge

class WedgeListFragment : Fragment(), WedgeAdapter.Listener {
    private lateinit var binding: FragmentWedgeListBinding
    private val adapter = WedgeAdapter(this)
    private val viewModel: WedgeViewModel by viewModels {
        WedgeViewModelFactory((requireActivity().application as WedgesApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wedge_list, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel.allWedges.observe(requireActivity()) { adapter.setList(it) }
        binding.apply {
            recyclerView.adapter = adapter
            lifecycleOwner = viewLifecycleOwner
            listViewModel = viewModel
        }
    }

    override fun onLongClick(view: View, wedge: Wedge): Boolean {
        showContextPopupMenu(view, wedge)

        return true
    }

    override fun onClick(view: View, wedge: Wedge) {
        findNavController().navigate(
            WedgeListFragmentDirections.actionWedgeListFragmentToWedgeEditActivity(wedge)
        )
    }

    private fun showContextPopupMenu(item: View, wedge: Wedge) {
        PopupMenu(requireContext(), item).apply {
            inflate(R.menu.item_context_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_context_menu_delete -> {
                        confirmDeleteWedge(wedge)
                    }
                    R.id.item_context_menu_send -> {
                        makeShareIntent(wedge)
                    }
                }
                true
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setForceShowIcon(true)
            }
            show()
        }
    }

    private fun confirmDeleteWedge(wedge: Wedge) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setIcon(R.drawable.ic_item_context_menu_delete_24)
            setTitle(getString(R.string.confirm_delete_dialog_title))
            setMessage(
                "${
                    getString(
                        R.string.confirm_delete_dialog_message,
                        wedge.getFormattedDate()
                    )
                }?"
            )
            setPositiveButton(getString(R.string.confirm_delete_dialog_yes)) { _, _ ->
                deleteWedgeOnDB(wedge)
            }
            setNegativeButton(getString(R.string.confirm_delete_dialog_no)) { _, _ -> }
            show()
        }
    }

    private fun deleteWedgeOnDB(wedge: Wedge) {
        viewModel.apply {
            deleteWedge(wedge)
            deleteTotal(Total(wedge.date))
        }
    }

    private fun makeShareIntent(wedge: Wedge) {
        with(wedge) {
            viewModel.getSumWedgesOnDate(date).observeOnce(requireActivity()) { sumWedgesOnDate ->
                val text =
                    getString(R.string.share_intent_date, getFormattedDate()) +
                            getString(R.string.share_intent_wedge, sum - addExtra) +
                            getString(R.string.share_intent_add_extra, addExtra) +
                            if (invest > 0) {
                                getString(R.string.share_intent_invest, invest)
                            } else {
                                ""
                            } +
                            getString(R.string.share_intent_sum, sumWedgesOnDate)
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, text)
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                if (t != null) {
                    removeObserver(this)
                }
                observer.onChanged(t)
            }
        })
    }


}