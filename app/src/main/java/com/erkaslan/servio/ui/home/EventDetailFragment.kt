package com.erkaslan.servio.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.AllUtil
import com.erkaslan.servio.MainActivity
import com.erkaslan.servio.databinding.FragmentEventDetailBinding
import com.erkaslan.servio.model.Event
import com.erkaslan.servio.model.GenericResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailFragment (var event: Event) : Fragment() {
    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel
    var util = AllUtil()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        initViews()
        initObservers()
    }

    fun initViews(){
        binding.event = event
        util.glide(requireContext(), Uri.parse(event.picture), binding.ivEventDetail)
        if(event.organizer == (activity as MainActivity).currentUser?.pk) {
            binding.isCurrentUser = true
            binding.organizer = (activity as MainActivity).currentUser
        } else {
            binding.isCurrentUser = false
            detailViewModel.getProvider(event.organizer)
            Log.d("REQS", event.attendees.toString())

        }
    }

    fun initObservers(){
        detailViewModel.providerMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    binding.organizer = it.data
                }
                is GenericResult.Failure -> { }
                else -> {}
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}