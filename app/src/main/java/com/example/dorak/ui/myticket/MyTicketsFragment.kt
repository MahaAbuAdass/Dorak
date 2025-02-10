package com.example.dorak.ui.myticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dorak.databinding.MyTicketsBinding
import com.example.dorak.dataclass.BranchResponse
import com.example.dorak.dataclass.MyTicketResponse
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.ui.home.BranchAdapter
import com.example.dorak.ui.home.PayBillFragmentDirections
import com.example.dorak.util.PreferenceManager
import com.example.dorak.viewmodels.MyTicketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyTicketsFragment :Fragment() {
    private lateinit var binding : MyTicketsBinding
    private lateinit var myTicketViewModel : MyTicketViewModel
    var myTicketAdapter: MyTicketAdapter? = null

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

//        binding.imgBack.setOnClickListener {
//            requireActivity().onBackPressedDispatcher.onBackPressed()
//        }

        callMyTicketApi()
        observerMyTicketViewModel()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Exit the app when back is pressed in `nav_my_ticket`
                requireActivity().finishAffinity()
            }
        })

    }
    private fun callMyTicketApi() {
        val userId= PreferenceManager.getUserId(requireContext())
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){

            myTicketViewModel.getMyTicket(userId?:"")
        }
    }
    private fun observerMyTicketViewModel() {
        myTicketViewModel.myTicketResponse.observe(viewLifecycleOwner){myTicketList->
            myTicketListAdapter(myTicketList)
        }

        myTicketViewModel.errorResponse.observe(viewLifecycleOwner){

        }
    }

    private fun myTicketListAdapter(myTicket : List<MyTicketResponse>){
        myTicketAdapter = MyTicketAdapter(myTicket )
        binding.myTicketRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.myTicketRecyclerView.adapter = myTicketAdapter
    }
}