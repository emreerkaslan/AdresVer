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
import com.erkaslan.servio.databinding.FragmentServiceDetailBinding
import com.erkaslan.servio.model.GenericResult
import com.erkaslan.servio.model.Service
import com.erkaslan.servio.model.User
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class ServiceDetailFragment (var service: Service) : Fragment(), ServiceActionListener {
    private var _binding: FragmentServiceDetailBinding? = null
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
        binding.service = service
        util.glide(requireContext(), Uri.parse(service.picture), binding.ivServiceDetail)
        binding.recurring = if(service.recurring == true) "Yes" else "No"
        if(service.giver == (activity as MainActivity).currentUser?.pk) {
            binding.isCurrentUser = true
            binding.giver = (activity as MainActivity).currentUser
            val map = HashMap<String, List<Int>?>()
            map.put("data", service.requests)
            val gson = Gson()
            val json = JSONObject(gson.toJson(map))
            detailViewModel.getRequesters(json)
        } else {
            binding.isCurrentUser = false
            detailViewModel.getProvider(service.giver)
            binding.btnSendRequest.setOnClickListener {
                (activity as MainActivity).currentUser?.pk?.let { self -> detailViewModel.addRequest(service.pk, self) }
            }
        }
    }

    fun initObservers(){
        detailViewModel.providerMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    binding.giver = it.data
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
                    detailViewModel.getRequesters(json)
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
                    }
                    (activity as MainActivity).currentUser = user
                    binding.tvServiceDetailOthers.visibility = View.VISIBLE
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
        detailViewModel.acceptRequest(service.pk, user.pk)
    }

    override fun onDeclineRequest(user: User) {
        detailViewModel.declineRequest(service.pk, user.pk)
    }
}

interface ServiceActionListener{
    fun onAcceptRequest(user: User)
    fun onDeclineRequest(user: User)
}