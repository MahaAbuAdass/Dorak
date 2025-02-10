package com.example.dorak.ui.home.bookticket

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dorak.databinding.BookTicketFragmentBinding
import com.example.dorak.dataclass.ServicesResponse
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.ui.home.GetTicketFragmentDirections
import com.example.dorak.ui.home.ServiceAdapter
import com.example.dorak.viewmodels.GetAllServicesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookTicket :Fragment() {

    private lateinit var binding : BookTicketFragmentBinding

    private lateinit var servicesViewModel : GetAllServicesViewModel
    private var serviceAdapter: ServiceAdapter? = null

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

        val getServicesFactory = GenericViewModelFactory(GetAllServicesViewModel::class) {
            GetAllServicesViewModel(requireContext())
        }

        servicesViewModel = ViewModelProvider(this, getServicesFactory).get(GetAllServicesViewModel::class.java)

        callGetAllServicesApi()
        observerGetAllServiceViewModel()

//        binding.viewTicket.setOnClickListener {
//            findNavController().navigate(R.id.action_bookTicketFragment_to_serviceDetailsFragment)
//        }

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
    private fun observerGetAllServiceViewModel() {
        servicesViewModel.servicesResponse.observe(viewLifecycleOwner){servicesList->
            servicesListAdapter(servicesList)
        }
        servicesViewModel.errorResponse.observe(viewLifecycleOwner){
            Log.v("service list error","service list error")
        }

    }

    private fun servicesListAdapter(servicesList: List<ServicesResponse?>) {

        serviceAdapter = ServiceAdapter(servicesList , onItemClick = {
            findNavController().navigate(BookTicketDirections.actionBookTicketFragmentToPayBillBookTicketFragment(
                it.Qid.toString() , it.QNameEn ?:"",it.QNameAr?:""))
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = serviceAdapter

    }

    private fun callGetAllServicesApi() {
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.IO){
            servicesViewModel.getAllServices()
        }
    }
}