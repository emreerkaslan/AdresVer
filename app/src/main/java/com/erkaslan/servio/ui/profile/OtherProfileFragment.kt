package com.erkaslan.servio.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.model.*
import dagger.hilt.android.AndroidEntryPoint
import com.erkaslan.servio.AllUtil
import com.erkaslan.servio.MainActivity
import com.erkaslan.servio.databinding.FragmentOtherProfileBinding
import com.erkaslan.servio.ui.home.MyEventsFragment
import com.erkaslan.servio.ui.home.MyServicesFragment


@AndroidEntryPoint
class OtherProfileFragment(var user: User) : Fragment() {

    private var _binding: FragmentOtherProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private var util = AllUtil()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        initViews()
        initObservers()
    }

    fun initViews() {
        binding.currentUser = user
        if((activity as MainActivity).currentUser?.following?.contains(user.pk) == true){
            binding.btnFollow.text = "Unfollow"
        }

        util.glideCircle(requireContext(), Uri.parse(user.profilePic), binding.ivOtherProfile)
        binding.btnOtherServices.setOnClickListener {
            val id = this.id
            fragmentManager?.commit {
                detach(this@OtherProfileFragment)
                replace(id, MyServicesFragment(false, user.pk))
            }
        }

        binding.btnOtherEvents.setOnClickListener {
            val id = this.id
            fragmentManager?.commit {
                detach(this@OtherProfileFragment)
                replace(id, MyEventsFragment(false, user.pk))
            }
        }

        binding.btnFollow.setOnClickListener {
            if(user.following?.contains(user.pk) == true){
              profileViewModel.unfollow((activity as MainActivity).token?.token ?: "", (activity as MainActivity).currentUser?.pk ?: 0, user.pk)
              binding.btnFollow.text = "Follow"
            } else {
                profileViewModel.follow((activity as MainActivity).token?.token ?: "", (activity as MainActivity).currentUser?.pk ?: 0, user.pk)
                binding.btnFollow.text = "Unfollow"
            }
        }
    }

    fun initObservers() {

        profileViewModel.currentUserMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
                    /*
                    (activity as MainActivity).currentUser = it.data
                    user = it.data
                    binding.currentUser = it.data

                     */
                }
                is GenericResult.Failure -> {
                }
                else -> {}
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOtherProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}