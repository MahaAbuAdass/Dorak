package com.example.dorak.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dorak.R
import com.example.dorak.databinding.BookTicketFragmentBinding

class BookTicket :Fragment() {

    private lateinit var binding : BookTicketFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=BookTicketFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewTicket.setOnClickListener {
            findNavController().navigate(R.id.action_bookTicketFragment_to_serviceDetailsFragment)

        }
        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

}