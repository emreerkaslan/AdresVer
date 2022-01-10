package com.erkaslan.servio.ui.search


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.MainActivity
import com.erkaslan.servio.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import com.shivtechs.maplocationpicker.LocationPickerActivity
import android.content.Intent
import androidx.fragment.app.commit
import com.erkaslan.servio.model.Event
import com.erkaslan.servio.model.GenericResult
import com.erkaslan.servio.model.Service
import com.shivtechs.maplocationpicker.MapUtility
import java.lang.Exception
import java.lang.StringBuilder
import android.widget.ArrayAdapter

import android.R
import android.widget.RadioButton
import android.widget.SpinnerAdapter
import com.erkaslan.servio.model.User
import com.erkaslan.servio.ui.home.*
import com.erkaslan.servio.ui.profile.OtherProfileFragment


@AndroidEntryPoint
class SearchFragment : Fragment(), HomeActionListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel
    var radio = "Service"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        initViews()
        initObservers()
    }

    fun initViews(){
        binding.item = radio
        binding.btnSearchService.setOnClickListener {
            radio = "Service"
            binding.item = radio
        }
        binding.btnSearchEvent.setOnClickListener {
            radio = "Event"
            binding.item = radio
        }
        binding.btnSearchUser.setOnClickListener {
            radio = "User"
            binding.item = radio
        }

        binding.btnSearch.setOnClickListener {
            when (radio) {
                "Service" -> { searchViewModel.searchService(binding.etSearch.text.toString()) }
                "Event" -> { searchViewModel.searchEvent(binding.etSearch.text.toString()) }
                "User" -> { searchViewModel.searchUser(binding.etSearch.text.toString()) }
            }
        }
    }

    fun initObservers() {
        searchViewModel.searchedServicesMutableLiveData?.observe(viewLifecycleOwner, {
            when (it) {
                is GenericResult.Success -> {
                    binding.rvSearch.adapter = ServiceListAdapter(it.data, this)
                    (binding.rvSearch.adapter as ServiceListAdapter).notifyDataSetChanged()
                }
                else -> { }
            }
        })

        searchViewModel.searchedEventsMutableLiveData?.observe(viewLifecycleOwner, {
            when (it) {
                is GenericResult.Success -> {
                    binding.rvSearch.adapter = EventListAdapter(it.data, this)
                    (binding.rvSearch.adapter as EventListAdapter).notifyDataSetChanged()
                }
                else -> { }
            }
        })

        searchViewModel.searchedUsersMutableLiveData?.observe(viewLifecycleOwner, {
            when (it) {
                is GenericResult.Success -> {
                    binding.rvSearch.adapter = UserListAdapter(it.data, this)
                    (binding.rvSearch.adapter as UserListAdapter).notifyDataSetChanged()
                }
                else -> { }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onServiceClicked(service: Service) {
        val id = this.id
        fragmentManager?.commit {
            detach(this@SearchFragment)
            replace(id, ServiceDetailFragment(service))
        }
    }

    override fun onEventClicked(event: Event) {
        val id = this.id
        fragmentManager?.commit {
            detach(this@SearchFragment)
            replace(id, EventDetailFragment(event))
        }
    }

    override fun onUserClicked(user: User) {
        val id = this.id
        fragmentManager?.commit {
            detach(this@SearchFragment)
            replace(id, OtherProfileFragment(user))
        }
    }
}