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
import com.erkaslan.servio.databinding.FragmentServiceDetailBinding
import com.erkaslan.servio.model.Feedback
import com.erkaslan.servio.model.GenericResult
import com.erkaslan.servio.model.Service
import com.erkaslan.servio.model.User
import com.erkaslan.servio.ui.profile.OtherProfileFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@AndroidEntryPoint
class ServiceDetailFragment (var service: Service) : Fragment(), ServiceActionListener {
    private var _binding: FragmentServiceDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel
    var util = AllUtil()
    private var takerAfterService: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        initViews()
        initObservers()
    }

    fun initViews(){
        binding.service = service
        util.glide(requireContext(), Uri.parse(service.picture), binding.ivServiceDetail)
        binding.recurring = if(service.recurring == true) "Yes" else "No"

        //My service
        if(service.giver == (activity as MainActivity).currentUser?.pk) {
            binding.isCurrentUser = true
            binding.giver = (activity as MainActivity).currentUser
            val map = HashMap<String, List<Int>?>()
            map.put("data", service.requests)
            val gson = Gson()
            val json = JSONObject(gson.toJson(map))
            detailViewModel.getRequesters((activity as MainActivity).token?.token ?:"", json)
        }
        //Other users service
        else {
            binding.isCurrentUser = false
            detailViewModel.getProvider(service.giver)
            val calendar = Calendar.getInstance()

            //Service is active
            if(service.date ?: calendar.time > calendar.time) {
                if(service.taker != null && (service.taker ?: listOf()).contains((activity as MainActivity).currentUser?.pk ?: 0)) {
                    binding.btnSendRequest.visibility = View.GONE
                    binding.tvServiceDetailOthers.text = "Your request is accepted, save the date"
                    binding.ratingServiceDetailFeedback.visibility = View.GONE
                    binding.etServiceDetailOthersFeedback.visibility = View.GONE
                } else if(service.requests != null && (service.requests ?: listOf()).contains((activity as MainActivity).currentUser?.pk ?: 0)) {
                    binding.ratingServiceDetailFeedback.visibility = View.GONE
                    binding.etServiceDetailOthersFeedback.visibility = View.GONE
                    binding.tvServiceDetailOthers.visibility = View.GONE
                    binding.btnSendRequest.visibility = View.GONE
                    binding.tvServiceDetailOthers.text = "Request pending, your credits will be deposited back if not approved"
                } else {
                    binding.ratingServiceDetailFeedback.visibility = View.GONE
                    binding.etServiceDetailOthersFeedback.visibility = View.GONE
                    binding.tvServiceDetailOthers.visibility = View.GONE
                    binding.btnSendRequest.visibility = View.VISIBLE
                    binding.btnSendRequest.setOnClickListener {
                        binding.tvServiceDetailOthers.visibility = View.VISIBLE
                        binding.btnSendRequest.visibility = View.GONE
                        (activity as MainActivity).currentUser?.pk?.let { self -> detailViewModel.addRequest((activity as MainActivity).token?.token ?:"", service.pk, self) }
                    }
                }
            }
            //Service date is past
            else {
                val map = HashMap<String, List<Int>?>()
                map.put("data", service.feedbackList)
                val json = JSONObject(Gson().toJson(map))
                if(service.taker != null && (service.taker ?: listOf()).contains((activity as MainActivity).currentUser?.pk ?: 0)) {
                    takerAfterService = true
                }
                detailViewModel.getFeedbacks(json)
            }
        }
    }

    fun initObservers(){
        detailViewModel.providerMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    val provider = it.data
                    binding.giver = provider
                    util.glideCircle(requireContext(), Uri.parse(it.data.profilePic), binding.ivServiceDetailGiver)
                    if(provider.pk != (activity as MainActivity).currentUser?.pk) {
                        binding.containerServiceDetailGiver.setOnClickListener {
                            val id = this.id
                            fragmentManager?.commit {
                                detach(this@ServiceDetailFragment)
                                replace(id, OtherProfileFragment(provider))
                            }
                        }
                    }
                }
                is GenericResult.Failure -> { }
                else -> {}
            }
        })

        detailViewModel.requestersMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    binding.rvServiceDetailRequests.adapter = RequestListAdapter(it.data, this)
                }
                is GenericResult.Failure -> { }
                else -> {}
            }
        })

        detailViewModel.serviceMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    service = it.data
                    val map = HashMap<String, List<Int>?>()
                    map.put("data", service.requests)
                    val gson = Gson()
                    val json = JSONObject(gson.toJson(map))
                    detailViewModel.getRequesters((activity as MainActivity).token?.token ?:"", json)
                }
                is GenericResult.Failure -> { }
                else -> {}
            }
        })

        detailViewModel.requestedServiceMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    val user = (activity as MainActivity).currentUser
                    if(user!=null){
                        user.credits = user.credits - service.credits
                        detailViewModel.addCredits((activity as MainActivity).token?.token ?: "", service.giver, -service.credits)
                    }
                    (activity as MainActivity).currentUser = user
                    binding.tvServiceDetailOthers.visibility = View.VISIBLE
                }
                is GenericResult.Failure -> { }
                else -> {}
            }
        })

        detailViewModel.feedbackListMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    //Feedback List set
                    Log.d("FEED3", it.data.toString())
                    binding.rvServiceDetailFeedbacks.adapter = FeedbackListAdapter(it.data)
                    (binding.rvServiceDetailFeedbacks.adapter as FeedbackListAdapter).notifyDataSetChanged()

                    if(takerAfterService) {
                        val feedbackGiven: Boolean = (activity as MainActivity).currentUser?.pk in it.data.map { it.giver }
                        if(feedbackGiven) {
                            binding.etServiceDetailOthersFeedback.visibility = View.GONE
                            binding.ratingServiceDetailFeedback.visibility = View.GONE
                        }
                        //Feedback Not given yet
                        else {
                            binding.btnSendRequest.visibility = View.GONE
                            binding.ratingServiceDetailFeedback.visibility = View.VISIBLE
                            binding.etServiceDetailOthersFeedback.visibility = View.VISIBLE
                            binding.tvServiceDetailOthers.visibility = View.VISIBLE
                            binding.tvServiceDetailOthers.text = "Send Feedback"
                            binding.tvServiceDetailOthers.isAllCaps = true
                            binding.tvServiceDetailOthers.setOnClickListener {
                                val rating = binding.ratingServiceDetailFeedback.numStars
                                val comment = binding.etServiceDetailOthersFeedback.text.toString()
                                val json = JsonObject()
                                json.addProperty("service", service.pk)
                                json.addProperty("taker", service.giver)
                                json.addProperty("comment", binding.etServiceDetailOthersFeedback.text.toString())
                                json.addProperty("rating", binding.ratingServiceDetailFeedback.numStars)
                                json.addProperty("giver", (activity as MainActivity).currentUser?.pk)
                                detailViewModel.addFeedback((activity as MainActivity).token?.token ?: "", json)
                            }
                        }
                    }
                    //After service date and no currentUser interaction
                    else {
                        binding.btnSendRequest.visibility = View.GONE
                        binding.tvServiceDetailOthers.text = "This is a past service"
                        binding.ratingServiceDetailFeedback.visibility = View.GONE
                        binding.etServiceDetailOthersFeedback.visibility = View.GONE
                    }
                }
                is GenericResult.Failure -> { }
                else -> {}
            }
        })

        detailViewModel.feedbackMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    Log.d("FEED3", it.data.toString())
                    val currentFeedbacks = (binding.rvServiceDetailFeedbacks.adapter as FeedbackListAdapter).feedbackList.toMutableList()
                    currentFeedbacks.add(it.data)
                    binding.rvServiceDetailFeedbacks.adapter = FeedbackListAdapter(currentFeedbacks)
                    binding.feedbackGiven = true
                    detailViewModel.addServiceFeedback((activity as MainActivity).token?.token ?: "", service.pk, it.data.pk)
                    detailViewModel.addCredits((activity as MainActivity).token?.token ?: "", service.giver, service.credits)
                }
                is GenericResult.Failure -> { }
                else -> {}
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentServiceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAcceptRequest(user: User) {
        detailViewModel.acceptRequest((activity as MainActivity).token?.token ?:"", service.pk, user.pk)
    }

    override fun onDeclineRequest(user: User) {
        detailViewModel.declineRequest((activity as MainActivity).token?.token ?:"", service.pk, user.pk)
        detailViewModel.addCredits((activity as MainActivity).token?.token ?: "", user.pk, service.credits)
    }
}

interface ServiceActionListener{
    fun onAcceptRequest(user: User)
    fun onDeclineRequest(user: User)
}