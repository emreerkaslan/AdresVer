package com.erkaslan.servio.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.databinding.FragmentHomeBinding
import com.erkaslan.servio.manager.RunOnceManager
import com.erkaslan.servio.model.*
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.commit


@AndroidEntryPoint
class HomeFragment : Fragment(), HomeActionListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    lateinit var userService: UserService
    lateinit var userSingle: MutableLiveData<User>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        initViews()
        initObservers()
    }

    fun initViews(){
        binding.rvHomepage.adapter = HomeAdapter(this)
        homeViewModel.getAllServices()
        homeViewModel.getAllEvents()

        binding.textHome.setOnClickListener {
            context?.let { it -> RunOnceManager().runOnce(it) }
        }
    }

    fun initObservers(){

        homeViewModel.allServicesMutableLiveData?.observe(viewLifecycleOwner, {
            when(it) {
                is GenericResult.Success -> {
                    val listData = it.data
                    if (listData != null && listData.size > 0 ) {
                        val index = (binding.rvHomepage.adapter as HomeAdapter).homeRowList.let {
                            it.indexOf(it.filterIsInstance<HomeRowItem.ServicesAround>().firstOrNull())
                        }
                        if(index > -1){
                            (binding.rvHomepage.adapter as HomeAdapter).homeRowList[index] = HomeRowItem.ServicesAround(listData)
                        } else {
                            (binding.rvHomepage.adapter as HomeAdapter).homeRowList.add(HomeRowItem.ServicesAround(listData))
                        }
                        (binding.rvHomepage.adapter as HomeAdapter).notifyDataSetChanged()
                    }else {
                        Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show()
                    }
                }
                is GenericResult.Failure -> {}
                else -> {}
            }
        })

        homeViewModel.allEventsMutableLiveData.observe(viewLifecycleOwner, {
            when(it) {
                is GenericResult.Success -> {
                    Log.d("EX", "Events: " + it.data.toString())
                    val listData = it.data
                    if (listData != null && listData.size > 0 ) {
                        val index = (binding.rvHomepage.adapter as HomeAdapter).homeRowList.let {
                            it.indexOf(it.filterIsInstance<HomeRowItem.EventsAround>().firstOrNull())
                        }
                        if(index > -1){
                            (binding.rvHomepage.adapter as HomeAdapter).homeRowList[index] = HomeRowItem.EventsAround(listData)
                        } else {
                            (binding.rvHomepage.adapter as HomeAdapter).homeRowList.add(HomeRowItem.EventsAround(listData))
                        }
                        (binding.rvHomepage.adapter as HomeAdapter).notifyDataSetChanged()
                    }else {
                        Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show()
                    }
                }
                is GenericResult.Failure -> {}
                else -> {}
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onServiceClicked(service: Service) {
        val id = this.id
        fragmentManager?.commit {
            detach(this@HomeFragment)
            replace(id, ServiceDetailFragment(service))
        }
    }

    override fun onEventClicked(event: Event) {
        val id = this.id
        fragmentManager?.commit {
            detach(this@HomeFragment)
            replace(id, EventDetailFragment(event))
        }
    }
}

interface HomeActionListener {
    fun onServiceClicked(service: Service)
    fun onEventClicked(event: Event)
}