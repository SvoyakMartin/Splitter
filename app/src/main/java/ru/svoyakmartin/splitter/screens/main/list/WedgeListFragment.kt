package ru.svoyakmartin.splitter.screens.main.list

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import ru.svoyakmartin.splitter.*
import ru.svoyakmartin.splitter.databinding.FragmentWedgeListBinding
import ru.svoyakmartin.splitter.model.Total
import ru.svoyakmartin.splitter.model.Wedge
import ru.svoyakmartin.splitter.screens.add.WedgeEditActivity
import ru.svoyakmartin.splitter.util.util

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
        binding = FragmentWedgeListBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun init() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
            val i = Intent(activity, WedgeEditActivity::class.java)
            i.putExtra("wedge", wedge)
            startActivity(i)
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
                        deleteWedge(wedge)
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

    private fun deleteWedge(wedge: Wedge) {
        viewModel.apply {
            with(wedge) {
                deleteWedge(this)
                deleteTotal(Total(date, sum, invest))
            }
        }
    }

    private fun makeShareIntent(wedge: Wedge) {
        with(wedge) {
            viewModel.getSumWedgesOnDate(date).observeOnce(requireActivity()) { sumWedgesOnDate ->
                val text =
                    "ОТЧЁТ за ${util.getFormattedDate(date)}:\n" +
                            "Сумма от правила клина: ${util.num2String(sum - addExtra)}\n" +
                            "Заработано дополнительно: ${util.num2String(addExtra)}\n" +
                            if (invest > 0) {
                                "Инвестировано ${util.num2String(invest)}\n"
                            } else {
                                ""
                            } +
                            "Итого на инвестиции: $sumWedgesOnDate"
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