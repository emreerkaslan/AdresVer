package com.erkaslan.servio.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.MainActivity
import com.erkaslan.servio.databinding.FragmentMyEventsBinding
import com.erkaslan.servio.model.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyEventsFragment(var myEvent: Boolean, val user: Int)  : Fragment(), HomeActionListener {
    private var _binding: FragmentMyEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        initViews()
        initObservers()
    }

    fun initViews(){
        if((activity as MainActivity).currentUser!=null){
            if(!myEvent){
                binding.tvMyEventList.text = "Events"
                detailViewModel.getUserEvents((activity as MainActivity).token?.token ?: "", user)
            } else {
                detailViewModel.getUserEvents((activity as MainActivity).token?.token ?: "",(activity as MainActivity).currentUser?.pk ?: 0)
            }

        }
    }

    fun initObservers(){
        detailViewModel.userEventsMutableLiveData?.observe(viewLifecycleOwner, {
            when(it) {
                is GenericResult.Success -> {
                    binding.rvMyEventList.adapter = EventListAdapter(it.data, this)
                    (binding.rvMyEventList.adapter as EventListAdapter).notifyDataSetChanged()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onServiceClicked(service: Service) {
    }

    override fun onEventClicked(event: Event) {
        val id = this.id
        fragmentManager?.commit {
            detach(this@MyEventsFragment)
            replace(id, EventDetailFragment(event))
        }
    }

    override fun onUserClicked(user: User) {
    }
}