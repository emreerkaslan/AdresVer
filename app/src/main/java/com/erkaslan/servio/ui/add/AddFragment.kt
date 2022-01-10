package com.erkaslan.servio.ui.add

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.AllUtil
import com.erkaslan.servio.MainActivity
import com.erkaslan.servio.databinding.FragmentAddBinding
import com.erkaslan.servio.model.GenericResult
import com.google.gson.JsonObject
import java.util.*


class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var addViewModel: AddViewModel
    private var sharedPreferences: SharedPreferences? = context?.getSharedPreferences("app", Context.MODE_PRIVATE)
    private var util = AllUtil()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addViewModel = ViewModelProvider(this).get(AddViewModel::class.java)
        initViews()
        initObservers()
    }

    fun initViews(){
        binding.isService = true
        binding.btnServiceHeader.setOnClickListener{ binding.isService = true }
        binding.btnEventHeader.setOnClickListener{ binding.isService = false }

        binding.btnServiceCreate.setOnClickListener{
            createService()
        }

        binding.btnEventCreate.setOnClickListener{
            createEvent()
        }
    }

    fun initObservers(){
        addViewModel.serviceCreatedMutableLiveData.observe(viewLifecycleOwner, {
            when(it) {
                is GenericResult.Success -> {
                    Toast.makeText(context, "Service Created", Toast.LENGTH_SHORT).show()
                }
                is GenericResult.Failure -> {
                    Toast.makeText(context, "Something was wrong, try again", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        })

        addViewModel.eventCreatedMutableLiveData.observe(viewLifecycleOwner, {
            when(it) {
                is GenericResult.Success -> {
                    Toast.makeText(context, "Event Created", Toast.LENGTH_SHORT).show()
                }
                is GenericResult.Failure -> {
                    Toast.makeText(context, "Something was wrong, try again", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        })
    }
    
    private fun createService(){
        val calendar = Calendar.getInstance()
        var format = SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss'Z'"), Locale.getDefault())
        calendar.set(binding.dpServiceCreation.year, binding.dpServiceCreation.month, binding.dpServiceCreation.dayOfMonth, binding.tpServiceCreation.currentHour,
            binding.tpServiceCreation.currentMinute)

        if(calendar.time < Calendar.getInstance().time){
            Toast.makeText(context, "Picking a later date than now may be wise", Toast.LENGTH_LONG).show()
            return
        }

        if(binding.etServiceCreationCredits.text.toString().toIntOrNull() == null){
            Toast.makeText(context, "Enter a numeric value less than 15 for credits", Toast.LENGTH_LONG).show()
            return

        } else if (Integer.parseInt(binding.etServiceCreationCredits.text.toString()) > 15 || Integer.parseInt(binding.etServiceCreationCredits.text.toString()) <1) {
            Toast.makeText(context, "Enter a numeric value less than 15 for credits", Toast.LENGTH_LONG).show()
            return
        }

        val json = JsonObject()
        json.addProperty("title", binding.etServiceCreationTitle.text.toString())
        json.addProperty("description", binding.etServiceCreationDescription.text.toString())
        json.addProperty("credits", Integer.parseInt(binding.etServiceCreationCredits.text.toString()))
        json.addProperty("date", format.format(calendar.time).toString()) //"2022-01-14T05:03:00Z"
        json.addProperty("geolocation", binding.etServiceCreationGeolocation.text.toString())
        json.addProperty("giver", (activity as MainActivity)?.currentUser?.pk)
        val tags = binding.etServiceCreationTags.text.toString().split(" ", ",", "#")
        //json.addProperty("tags", binding.etServiceCreationTags.text.toString())

        if(binding.cbServiceCreationRecurring.isChecked){
            json.addProperty("recurring", true)
        }else{
            json.addProperty("recurring", false)
        }
        //val result = util.validateServiceCreation(json)
        addViewModel.createService((activity as MainActivity).token?.token ?: "", json)
    }

    private fun createEvent(){
        if(((activity as MainActivity).currentUser?.service?.filter { (it.date ?: Calendar.getInstance().time) > Calendar.getInstance().time } ?: listOf()).size >=10) {
            Toast.makeText(context, "You can provide maximum of 10 services at a time", Toast.LENGTH_LONG).show()
        }
        val calendar = Calendar.getInstance()
        var format = SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss'Z'"), Locale.getDefault())
        calendar.set(binding.dpEventCreation.year, binding.dpEventCreation.month, binding.dpEventCreation.dayOfMonth, binding.tpEventCreation.currentHour,
            binding.tpEventCreation.currentMinute)

        if(calendar.time < Calendar.getInstance().time){
            Toast.makeText(context, "Picking a later date than now may be wise", Toast.LENGTH_LONG).show()
            return
        }

        val json = JsonObject()
        json.addProperty("title", binding.etEventCreationTitle.text.toString())
        json.addProperty("description", binding.etEventCreationDescription.text.toString())
        json.addProperty("date", format.format(calendar.time).toString()) //"2022-01-14T05:03:00Z"
        json.addProperty("geolocation", binding.etEventCreationGeolocation.text.toString())
        json.addProperty("address", binding.etEventCreationAddress.text.toString())
        json.addProperty("organizer", (activity as MainActivity)?.currentUser?.pk)

        if(binding.cbEventCreationQuota.isChecked){
            json.addProperty("hasQuota", true)
            json.addProperty("quota", binding.etEventCreationQuota.text.toString())
        }else{
            json.addProperty("hasQuota", false)
        }
        Log.d("CALL",json.toString())
        addViewModel.createEvent((activity as MainActivity).token?.token ?: "", json)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}