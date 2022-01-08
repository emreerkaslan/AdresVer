package com.erkaslan.servio.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.AllUtil
import com.erkaslan.servio.MainActivity
import com.erkaslan.servio.databinding.FragmentEventDetailBinding
import com.erkaslan.servio.model.Event
import com.erkaslan.servio.model.GenericResult
import com.erkaslan.servio.ui.profile.OtherProfileFragment
import com.erkaslan.servio.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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
            binding.btnEventDetailOthers.visibility=View.GONE
            binding.tvEventDetailOthers.visibility=View.GONE
            binding.organizer = (activity as MainActivity).currentUser
        } else {
            binding.isCurrentUser = false
            detailViewModel.getProvider(event.organizer)
            Log.d("REQS", event.attendees.toString())
            val calendar = Calendar.getInstance()

            //Event is active
            if(event.date ?: calendar.time > calendar.time) {
                if((event.attendees ?: listOf()).contains((activity as MainActivity).currentUser?.pk ?: 0) ) {
                    binding.btnEventDetailOthers.visibility=View.GONE
                    binding.tvEventDetailOthers.text = "You are attending, save the date!"
                } else if(event.hasQuota && event.quota == event.attendees?.size) {
                    binding.btnEventDetailOthers.visibility = View.GONE
                    binding.tvEventDetailOthers.text = "Quota is full"
                } else {
                    binding.tvEventDetailOthers.visibility = View.GONE
                    binding.btnEventDetailOthers.setOnClickListener {
                        detailViewModel.attend((activity as MainActivity).token?.token ?: "", event.pk, (activity as MainActivity).currentUser?.pk ?: 0 )
                    }
                }
            } else {
                binding.btnEventDetailOthers.visibility = View.GONE
                binding.tvEventDetailOthers.text = "This is a past event"
            }

        }
    }

    fun initObservers(){
        detailViewModel.eventMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    binding.event = it.data
                    binding.btnEventDetailOthers.visibility = View.GONE
                    binding.tvEventDetailOthers.text = "You are attending, save the date!"
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