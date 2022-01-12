package com.erkaslan.servio.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erkaslan.servio.databinding.RowLayoutEventListBinding
import com.erkaslan.servio.databinding.RowLayoutServiceListBinding
import com.erkaslan.servio.model.Event
import com.erkaslan.servio.model.Service
import java.lang.Exception

class HomeAdapter(val listener: HomeActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        const val TYPE_SERVICES_AROUND = 0
        const val TYPE_EVENTS_AROUND = 1
    }

    lateinit var layoutInflater: LayoutInflater

    val homeRowList: MutableList<HomeRowItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(!::layoutInflater.isInitialized){ layoutInflater = LayoutInflater.from(parent.context) }
        return when(viewType){
            TYPE_SERVICES_AROUND -> {
                ServiceViewHolder.create(layoutInflater, parent)
            }
            TYPE_EVENTS_AROUND -> {
                EventViewHolder.create(layoutInflater, parent)
            }
            else -> {
                throw Exception("View type not expected")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = homeRowList[position]
        when(holder) {
            is ServiceViewHolder -> {
                (item as HomeRowItem.ServicesAround).let {
                    holder.bind(it, listener)
                }
            }
            is EventViewHolder -> {
                (item as HomeRowItem.EventsAround).let {
                    holder.bind(it, listener)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return homeRowList.size
    }

    override fun getItemViewType(position: Int): Int {
        return homeRowList[position].type
    }

}

class ServiceViewHolder(val binding: RowLayoutServiceListBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup): ServiceViewHolder{
            return ServiceViewHolder(RowLayoutServiceListBinding.inflate(inflater, parent, false))
        }
    }

    fun bind(serviceList: HomeRowItem.ServicesAround, listener: HomeActionListener){
        binding.rvServiceList.adapter = ServiceListAdapter(serviceList.services, listener)
    }
}

class EventViewHolder(val binding: RowLayoutEventListBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup): EventViewHolder{
            return EventViewHolder(RowLayoutEventListBinding.inflate(inflater, parent, false))
        }
    }

    fun bind(eventList: HomeRowItem.EventsAround, listener: HomeActionListener){
        binding.rvEventList.adapter = EventListAdapter(eventList.events, listener)
    }
}

sealed class HomeRowItem(val type: Int){
    data class ServicesAround(var services: List<Service>) : HomeRowItem(HomeAdapter.TYPE_SERVICES_AROUND)
    data class EventsAround(var events: List<Event>) : HomeRowItem(HomeAdapter.TYPE_EVENTS_AROUND)
}