package com.erkaslan.servio.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erkaslan.servio.databinding.RowServiceBinding
import com.erkaslan.servio.model.Service

class SearchAdapter(var list: List<Any>) : RecyclerView.Adapter<SearchViewHolder>() {

    lateinit var layoutInflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        if(!::layoutInflater.isInitialized){
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return SearchViewHolder.create(layoutInflater, parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val service = list[position]
        holder.bind(service as Service)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

sealed class HomeRowItem(val type: Int){
    //data class User() : HomeRowItem(HomeAdapter.TYPE_USER)
}

class SearchViewHolder(val binding: RowServiceBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object{
        fun create(inflater: LayoutInflater, parent: ViewGroup): SearchViewHolder{
            return SearchViewHolder(RowServiceBinding.inflate(inflater, parent, false))
        }
    }

    fun bind(service: Service){
        binding.tvTitle.text = service.title
    }
}