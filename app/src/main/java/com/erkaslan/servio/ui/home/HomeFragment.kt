package com.erkaslan.servio.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.databinding.FragmentHomeBinding
import com.erkaslan.servio.manager.RunOnceManager
import com.erkaslan.servio.model.GenericResult
import com.erkaslan.servio.model.RetrofitClient
import com.erkaslan.servio.model.User
import com.erkaslan.servio.model.UserService
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class HomeFragment : Fragment() {

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
        binding.rvHomepage.adapter = HomeAdapter()
        homeViewModel.getAllServices()

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
                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
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

    fun getUser(){
        userService = RetrofitClient.getClient().create(UserService::class.java)
        var users = userService.getUsers(5)

        users.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d("EX", response.body().toString())
                if (response.isSuccessful) {
                    userSingle = response.body() as MutableLiveData<User>
                    binding.textHome.text = userSingle.toString()
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                } else if (response.code() == 400) {
                    Log.v("VER",response.errorBody().toString());
                }
                else{
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
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
}