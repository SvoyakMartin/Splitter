package ru.svoyakmartin.splitter.screens.main.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.svoyakmartin.splitter.R
import ru.svoyakmartin.splitter.model.Wedge
import ru.svoyakmartin.splitter.databinding.WedgeItemBinding
import ru.svoyakmartin.splitter.util.util

class WedgeAdapter(private val listener: Listener):RecyclerView.Adapter<WedgeAdapter.WedgeHolder>() {
    private var wedgesList = ArrayList<Wedge>()

    class WedgeHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = WedgeItemBinding.bind(item)

        fun bind(wedge: Wedge, listener: Listener){
            binding.apply {
                with(wedge){
                    itemDate.text = util.getFormattedDate(date)
                    itemSum.text = util.num2String(sum)
                }
                listener.setListeners(item, wedge)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WedgeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wedge_item, parent, false)

        return WedgeHolder(view)
    }

    override fun onBindViewHolder(holder: WedgeHolder, position: Int) {
        holder.bind(wedgesList[position], listener)
    }

    override fun getItemCount(): Int {
        return wedgesList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(wedges: List<Wedge>){
        wedgesList = wedges as ArrayList<Wedge>
        notifyDataSetChanged()
    }

    interface Listener{
        fun setListeners(item: View, wedge: Wedge)
    }
}