package ru.svoyakmartin.splitter.screens.main.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.svoyakmartin.splitter.R
import ru.svoyakmartin.splitter.model.Wedge
import ru.svoyakmartin.splitter.databinding.WedgeItemBinding

class WedgeAdapter(private val listener: Listener) :
    RecyclerView.Adapter<WedgeAdapter.WedgeHolder>() {
    private var wedgesList = ArrayList<Wedge>()

    class WedgeHolder(private val binding: WedgeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(wedge: Wedge, listener: Listener) {
            binding.apply {
                this.wedge = wedge
                this.listener = listener
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WedgeHolder {
        val binding: WedgeItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.wedge_item, parent, false
            )

        return WedgeHolder(binding)
    }

    override fun onBindViewHolder(holder: WedgeHolder, position: Int) {
        holder.bind(wedgesList[position], listener)
    }

    override fun getItemCount(): Int = wedgesList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(wedges: List<Wedge>) {
        wedgesList = wedges as ArrayList<Wedge>
        notifyDataSetChanged()
    }

    interface Listener {
        fun onLongClick(view: View, wedge: Wedge): Boolean
        fun onClick(view: View, wedge: Wedge)
    }
}