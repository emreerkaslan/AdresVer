package com.erkaslan.servio.ui.add

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.AllUtil
import com.erkaslan.servio.databinding.FragmentAddBinding
import com.erkaslan.servio.model.GenericResult
import com.erkaslan.servio.model.Service
import java.util.*
import kotlin.collections.HashMap


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
            val map = HashMap<String, String>()
            val calendar = Calendar.getInstance()
            calendar.set(
                binding.dpServiceCreation.year,
                binding.dpServiceCreation.month,
                binding.dpServiceCreation.dayOfMonth,
                binding.tpServiceCreation.currentHour,
                binding.tpServiceCreation.currentMinute
            )
            if(calendar.time < Calendar.getInstance().time){
                Toast.makeText(context, "Picking a later date than now may be wise", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var format = SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ss.SSSZ"), Locale.getDefault())

            val tags = binding.etServiceCreationTags.text.toString().split(" ", ",", "#")

            map.put("title", binding.etServiceCreationTitle.text.toString())
            map.put("description", binding.etServiceCreationDescription.text.toString())
            map.put("credits", binding.etServiceCreationCredits.text.toString())
            if(binding.cbServiceCreationRecurring.isChecked){
                map.put("recurring", "true")
            }else{
                map.put("recurring", "false")
            }
            map.put("date", format.format(calendar.time).toString())
            map.put("tags", tags.toString())
            //map.put("picture", binding.ivServiceCreation)
            map.put("geolocation", binding.etServiceCreationGeolocation.text.toString())
            val result = util.validateServiceCreation(map)
            if(result.get("result").equals("false")){
                Toast.makeText(context, result.get("message"), Toast.LENGTH_LONG)
            } else {
                //sharedPreferences?.getString("currentUser", "")?["pk"]
                result.put("giver", "14")
                val service = Service(pk = 14,
                    title = binding.etServiceCreationTitle.text.toString(),
                    description = binding.etServiceCreationDescription.text.toString(),
                    credits = Integer.parseInt(binding.etServiceCreationCredits.text.toString()),
                    recurring = false,
                    date = calendar.time,
                    tags = tags,
                    geolocation = binding.etServiceCreationGeolocation.text.toString(),
                    giver = 14
                )
                Log.d("EX", service.toString())
                addViewModel.createService(service)
            }
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}