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
import com.erkaslan.servio.databinding.FragmentOtherProfileBinding
import com.erkaslan.servio.ui.home.MyServicesFragment


@AndroidEntryPoint
class OtherProfileFragment(val user: User) : Fragment() {

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
        util.glideCircle(requireContext(), Uri.parse(user.profilePic), binding.ivOtherProfile)
        binding.btnOtherServices.setOnClickListener {
            val id = this.id
            fragmentManager?.commit {
                detach(this@OtherProfileFragment)
                replace(id, MyServicesFragment(false, user.pk))
            }
        }
    }

    fun initObservers() {

        profileViewModel.currentUserMutableLiveData?.observe(viewLifecycleOwner, {
            when(it){
                is GenericResult.Success -> {
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