package com.erkaslan.servio.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.MainActivity
import com.erkaslan.servio.databinding.FragmentMyServicesBinding
import com.erkaslan.servio.model.Event
import com.erkaslan.servio.model.GenericResult
import com.erkaslan.servio.model.Service
import com.erkaslan.servio.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyServicesFragment(var myService: Boolean, val user: Int)  : Fragment(), HomeActionListener {
    private var _binding: FragmentMyServicesBinding? = null
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
            if(!myService){
                binding.tvMyServiceList.text = "Services"
                detailViewModel.getUserServices((activity as MainActivity).token?.token ?: "", user)
            } else {
                detailViewModel.getUserServices((activity as MainActivity).token?.token ?: "",(activity as MainActivity).currentUser?.pk ?: 0)
            }

        }
    }

    fun initObservers(){
        detailViewModel.userServicesMutableLiveData?.observe(viewLifecycleOwner, {
            when(it) {
                is GenericResult.Success -> {
                    binding.rvMyServiceList.adapter = ServiceListAdapter(it.data, this)
                    (binding.rvMyServiceList.adapter as ServiceListAdapter).notifyDataSetChanged()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMyServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onServiceClicked(service: Service) {
        val id = this.id
        fragmentManager?.commit {
            detach(this@MyServicesFragment)
            replace(id, ServiceDetailFragment(service))
        }
    }

    override fun onEventClicked(event: Event) {
    }

    override fun onUserClicked(user: User) {
    }
}