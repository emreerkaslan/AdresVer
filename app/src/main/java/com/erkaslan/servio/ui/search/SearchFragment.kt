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
import com.shivtechs.maplocationpicker.MapUtility
import java.lang.Exception
import java.lang.StringBuilder


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel
    val ADDRESS_PICKER_REQUEST: Int = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        initViews()
        initObservers()
    }

    fun initViews(){
        //searchViewModel.getAllServices()
        binding.btnPicker.setOnClickListener {
            val i = Intent(activity, LocationPickerActivity::class.java)
            startActivityForResult(i, ADDRESS_PICKER_REQUEST)
        }
    }

    fun initObservers(){

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    val currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0)
                    val currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0)
                    val completeAddress = data.getBundleExtra("fullAddress")
                    /* data in completeAddress bundle
                    "fulladdress"
                    "city"
                    "state"
                    "postalcode"
                    "country"
                    "addressline1"
                    "addressline2"
                     */binding.tvLat.setText(
                        StringBuilder().append("addressline2: ").append(
                            completeAddress!!.getString("addressline2")
                        ).append("\ncity: ").append(
                            completeAddress.getString("city")
                        ).append("\npostalcode: ").append(
                            completeAddress.getString("postalcode")
                        ).append("\nstate: ").append(
                            completeAddress.getString("state")
                        ).toString()
                    )
                    binding.tvLen.setText(
                        StringBuilder().append("Lat:").append(currentLatitude).append("  Long:")
                            .append(currentLongitude).toString()
                    )
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}