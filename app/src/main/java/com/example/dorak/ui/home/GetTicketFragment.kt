package com.example.dorak.ui.home

import android.app.Service
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dorak.R
import com.example.dorak.databinding.GetTicketFragmentBinding
import com.example.dorak.dataclass.ServicesResponse
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.ui.login.services.GetAllServicesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetTicketFragment : Fragment() {

    private lateinit var binding : GetTicketFragmentBinding
    private lateinit var servicesViewModel : GetAllServicesViewModel

    var currentQAdapter: ServiceAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=GetTicketFragmentBinding.inflate(inflater,container,false)
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


//        binding.cardViewTicket1.setOnClickListener {
//            findNavController().navigate(R.id.action_getTicket_to_paybill)
//        }

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }


    private fun observerGetAllServiceViewModel() {
        servicesViewModel.servicesResponse.observe(viewLifecycleOwner){servicesList->
            servicesListAdapter(servicesList)

        }

    }

    fun servicesListAdapter(servicesList: List<ServicesResponse?>) {

        currentQAdapter = ServiceAdapter(servicesList , onItemClick = {
              findNavController().navigate(GetTicketFragmentDirections.actionGetTicketToPaybill(it.Qid.toString()))
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = currentQAdapter

    }

    private fun callGetAllServicesApi() {
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.IO){
            servicesViewModel.getAllServices()
        }
    }

}