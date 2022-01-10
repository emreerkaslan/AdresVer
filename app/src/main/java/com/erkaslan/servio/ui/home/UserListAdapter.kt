package com.erkaslan.servio.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erkaslan.servio.AllUtil
import com.erkaslan.servio.databinding.RowUserItemBinding
import com.erkaslan.servio.model.User

class UserListAdapter (var userList: List<User>, val listener: HomeActionListener): RecyclerView.Adapter<UserItemViewHolder>() {

    private lateinit var layoutInflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        if(!::layoutInflater.isInitialized){ layoutInflater = LayoutInflater.from(parent.context) }
        return UserItemViewHolder.create(layoutInflater, parent)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        holder.bind(userList[position], listener)
    }
}

class UserItemViewHolder(var binding: RowUserItemBinding): RecyclerView.ViewHolder(binding.root){
    companion object {
        val util = AllUtil()
        fun create (inflater: LayoutInflater, parent: ViewGroup): UserItemViewHolder {
            return UserItemViewHolder(RowUserItemBinding.inflate(inflater, parent, false))
        }
    }

    fun bind(user: User, listener: HomeActionListener) {
        binding.username = user.username
        if(user.profilePic != null) { util.glide(binding.ivUser.context, Uri.parse(user.profilePic), binding.ivUser) }
        binding.containerUser.setOnClickListener {
            listener.onUserClicked(user)
        }
    }
}