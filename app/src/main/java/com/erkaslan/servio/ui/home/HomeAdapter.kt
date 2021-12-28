package com.erkaslan.servio.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erkaslan.servio.databinding.RowServiceBinding
import com.erkaslan.servio.model.Service

class HomeAdapter(var serviceList: List<Service>) : RecyclerView.Adapter<HomeViewHolder>() {

    lateinit var layoutInflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        if(!::layoutInflater.isInitialized){
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return HomeViewHolder.create(layoutInflater, parent)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val service = serviceList[position]
        holder.bind(service)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

}

sealed class HomeRowItem(val type: Int){
    //data class User() : HomeRowItem(HomeAdapter.TYPE_USER)
}

class HomeViewHolder(val binding: RowServiceBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup): HomeViewHolder{
            return HomeViewHolder(RowServiceBinding.inflate(inflater, parent, false))
        }
    }

    fun bind(service: Service){
        binding.tvTitle.text = service.title
    }
}