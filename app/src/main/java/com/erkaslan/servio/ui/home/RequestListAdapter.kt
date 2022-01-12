package com.erkaslan.servio.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erkaslan.servio.databinding.RowLayoutRequestBinding
import com.erkaslan.servio.model.User

class RequestListAdapter (var userList: List<User>, var listener: ServiceActionListener): RecyclerView.Adapter<RequestItemViewHolder>() {

    private lateinit var layoutInflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestItemViewHolder {
        if(!::layoutInflater.isInitialized){ layoutInflater = LayoutInflater.from(parent.context) }
        return RequestItemViewHolder.create(layoutInflater, parent)
    }

    override fun getItemCount(): Int { return userList.size }

    override fun onBindViewHolder(holder: RequestItemViewHolder, position: Int) {
        holder.bind(userList[position], listener)
    }
}

class RequestItemViewHolder(val binding: RowLayoutRequestBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup): RequestItemViewHolder{
            return RequestItemViewHolder(RowLayoutRequestBinding.inflate(inflater, parent, false))
        }
    }

    fun bind(user: User, listener: ServiceActionListener) {
        binding.username = user.username
        binding.btnAcceptRequest.setOnClickListener { listener.onAcceptRequest(user) }
        binding.btnDeclineRequest.setOnClickListener { listener.onDeclineRequest(user) }
    }
}