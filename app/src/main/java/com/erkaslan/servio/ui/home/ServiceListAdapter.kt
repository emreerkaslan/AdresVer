package com.erkaslan.servio.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erkaslan.servio.databinding.RowServiceItemBinding
import com.erkaslan.servio.model.Service

class ServiceListAdapter (var serviceList: List<Service>): RecyclerView.Adapter<ServiceItemViewHolder>() {

    private lateinit var layoutInflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceItemViewHolder {
        if(!::layoutInflater.isInitialized){ layoutInflater = LayoutInflater.from(parent.context) }
        return ServiceItemViewHolder.create(layoutInflater, parent)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    override fun onBindViewHolder(holder: ServiceItemViewHolder, position: Int) {
        holder.bind(serviceList[position])
    }
}

class ServiceItemViewHolder(var binding: RowServiceItemBinding): RecyclerView.ViewHolder(binding.root){
    companion object {
        fun create (inflater: LayoutInflater, parent: ViewGroup): ServiceItemViewHolder {
            return ServiceItemViewHolder(RowServiceItemBinding.inflate(inflater, parent, false))
        }
    }

    fun bind(service: Service) {
        binding.service = service
        binding.credits = service.credits.toString()
        if(service.picture != null) { EventItemViewHolder.util.glide(binding.ivService.context, Uri.parse(service.picture), binding.ivService) }
    }
}