package com.erkaslan.servio.ui.home

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erkaslan.servio.AllUtil
import com.erkaslan.servio.databinding.RowEventItemBinding
import com.erkaslan.servio.model.Event

class EventListAdapter (var eventList: List<Event>): RecyclerView.Adapter<EventItemViewHolder>() {

    private lateinit var layoutInflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventItemViewHolder {
        if(!::layoutInflater.isInitialized){ layoutInflater = LayoutInflater.from(parent.context) }
        return EventItemViewHolder.create(layoutInflater, parent)
    }

    override fun getItemCount(): Int {
        return if(eventList.size<5) eventList.size else 5
    }

    override fun onBindViewHolder(holder: EventItemViewHolder, position: Int) {
        holder.bind(eventList[position])
    }
}

class EventItemViewHolder(var binding: RowEventItemBinding): RecyclerView.ViewHolder(binding.root){
    companion object {
        val util = AllUtil()
        fun create (inflater: LayoutInflater, parent: ViewGroup): EventItemViewHolder {
            return EventItemViewHolder(RowEventItemBinding.inflate(inflater, parent, false))
        }
    }

    fun bind(event: Event) {
        binding.event = event
        util.glide(binding.ivEvent.context, Uri.parse(event.picture), binding.ivEvent)
        if(event.hasQuota){
            binding.quota = event.quota.toString()
        }
    }
}