package com.example.dorak.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dorak.R
import com.example.dorak.databinding.TicketDetailsBinding
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.util.PreferenceManager
import com.example.dorak.viewmodels.GenerateTicketViewModel
import com.example.dorak.viewmodels.GetAvailableTimeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TicketDetails :Fragment() {

    private lateinit var binding : TicketDetailsBinding
    private lateinit var generateTicketViewModel : GenerateTicketViewModel
    private val args: TicketDetailsArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=TicketDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.tvSelectedService.text = args.serviceEn
//        binding.tvSelectedServiceAr.text = args.serviceAr


        val getTicketFactory = GenericViewModelFactory(GenerateTicketViewModel::class) {
            GenerateTicketViewModel(requireContext())
        }

        generateTicketViewModel = ViewModelProvider(this, getTicketFactory).get(GenerateTicketViewModel::class.java)

        callGenerateTicketApi()
        observerGenerateTicketViewModel()

        binding.btnMyTicket.setOnClickListener {
            findNavController().navigate(R.id.action_ticketDetails_to_myTicketFragment)
        }

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun observerGenerateTicketViewModel() {
        Log.v("generating the ticket","generating the ticket 1 ")

        generateTicketViewModel.generateTicketResponse.observe(viewLifecycleOwner){ticket->
            Log.v("responce for api is", ticket.toString())
            binding.tvBranchEn.text = ticket.BranchNameEn
            binding.tvBranchAr.text = ticket.BranchNameAr
            binding.tvTicketNumber.text = ticket.TicketNo

            Log.v("generating the ticket","generating the ticket 2")

        }

        generateTicketViewModel.errorResponse.observe(viewLifecycleOwner){
            Log.v("error generating the ticket",it.toString())
        }

    }

    private fun callGenerateTicketApi() {
        val qid = args.qid
        val branchCode = args.branchCode
        val userId = PreferenceManager.getUserId(requireContext())

        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.IO){
            generateTicketViewModel.getGeneratedTicket(qid,branchCode,userId?:"")
        }
    }
}