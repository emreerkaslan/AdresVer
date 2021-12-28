package com.erkaslan.servio.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.erkaslan.servio.R
import com.erkaslan.servio.databinding.FragmentHomeBinding
import com.erkaslan.servio.databinding.FragmentSearchBinding
import com.erkaslan.servio.ui.home.HomeViewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        searchViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }
}