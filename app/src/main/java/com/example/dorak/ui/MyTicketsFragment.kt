package com.example.dorak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.dorak.databinding.MyTicketsBinding
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.util.PreferenceManager
import com.example.dorak.viewmodels.BookTicketViewModel
import com.example.dorak.viewmodels.MyTicketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyTicketsFragment :Fragment() {
    private lateinit var binding : MyTicketsBinding
    private lateinit var myTicketViewModel : MyTicketViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= MyTicketsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val myTicketFactory = GenericViewModelFactory(MyTicketViewModel::class) {
            MyTicketViewModel(requireContext())
        }

        myTicketViewModel = ViewModelProvider(this, myTicketFactory).get(
            MyTicketViewModel::class.java)

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        callMyTicketApi()
        observerMyTicketViewModel()

    }
    private fun callMyTicketApi() {
        val userId= PreferenceManager.getUserId(requireContext())
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){

            myTicketViewModel.getMyTicket(userId?:"")
        }
    }
    private fun observerMyTicketViewModel() {
        myTicketViewModel.myTicketResponse.observe(viewLifecycleOwner){

        }

        myTicketViewModel.errorResponse.observe(viewLifecycleOwner){

        }

    }
}