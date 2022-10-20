package ru.svoyakmartin.splitter.screens.main.list

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.svoyakmartin.splitter.*
import ru.svoyakmartin.splitter.databinding.FragmentWedgeListBinding
import ru.svoyakmartin.splitter.model.Total
import ru.svoyakmartin.splitter.model.Wedge
import ru.svoyakmartin.splitter.screens.add.WedgeEditActivity
import ru.svoyakmartin.splitter.util.Util

class WedgeListFragment : Fragment(), WedgeAdapter.Listener {
    private var _binding: FragmentWedgeListBinding? = null
    private val binding get() = _binding!!
    private val adapter = WedgeAdapter(this)
    private val viewModel: WedgeViewModel by viewModels {
        WedgeViewModelFactory((requireActivity().application as WedgesApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWedgeListBinding.inflate(inflater)

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
        binding.apply {
            recyclerView.adapter = adapter

            viewModel.allWedges.observe(requireActivity()) { wedges ->
                adapter.setList(wedges)

                if (wedges.isNotEmpty()) {
                    recyclerView.visibility = View.VISIBLE
                    wedgeListPlaceHolder.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.GONE
                    wedgeListPlaceHolder.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun getInstance() = WedgeListFragment()
    }

    override fun setListeners(item: View, wedge: Wedge) {
        item.setOnClickListener {
            val action =
                WedgeListFragmentDirections.actionWedgeListFragmentToWedgeEditActivity(wedge)
            findNavController().navigate(action)
        }

        item.setOnLongClickListener {
            showContextPopupMenu(item, wedge)

            return@setOnLongClickListener true
        }
    }

    private fun showContextPopupMenu(item: View, wedge: Wedge) {
        PopupMenu(activity, item).apply {
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
        AlertDialog.Builder(requireContext()).apply {
            setPositiveButton(getString(R.string.confirm_delete_dialog_yes)) { _, _ ->
                deleteWedgeOnDB(wedge)
            }
            setNegativeButton(getString(R.string.confirm_delete_dialog_no)) { _, _ -> }
            setTitle(getString(R.string.confirm_delete_dialog_title))
            setMessage("${getString(R.string.confirm_delete_dialog_message)} ${Util.getFormattedDate(wedge.date)}?")
            create()
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
                    getString(R.string.share_intent_date, Util.getFormattedDate(date)) +
                            getString(R.string.share_intent_wedge, Util.num2String(sum - addExtra)) +
                            getString(R.string.share_intent_add_extra, Util.num2String(addExtra)) +
                            if (invest > 0) {getString(R.string.share_intent_invest, Util.num2String(invest))} else {""} +
                            getString(R.string.share_intent_sum, Util.num2String(sumWedgesOnDate))
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