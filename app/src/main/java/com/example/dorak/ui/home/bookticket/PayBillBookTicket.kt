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
import com.example.dorak.R
import com.example.dorak.databinding.PayBillBookTicketBinding
import com.example.dorak.dataclass.BranchResponse
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.ui.home.BranchAdapter
import com.example.dorak.ui.home.PayBillFragmentArgs
import com.example.dorak.ui.home.PayBillFragmentDirections
import com.example.dorak.viewmodels.GetBranchesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PayBillBookTicket :Fragment() {

    private lateinit var binding : PayBillBookTicketBinding

    private lateinit var branchesViewModel: GetBranchesViewModel
    var branchAdapter: BranchAdapter? = null

    private val args: PayBillBookTicketArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= PayBillBookTicketBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getBranchesFactory = GenericViewModelFactory(GetBranchesViewModel::class) {
            GetBranchesViewModel(requireContext())
        }

        branchesViewModel = ViewModelProvider(this, getBranchesFactory).get(GetBranchesViewModel::class.java)

        callGetAllBranchesApi()
        observerGetAllBranchesViewModel()

        binding.cardViewTicket1.setOnClickListener {
            val location = branchAdapter?.getTopBranch()?.BranchNameEn
            val branchCode = branchAdapter?.getTopBranch()?.BranchCode.toString()
            findNavController().navigate(PayBillBookTicketDirections.actionPayBillBookTicketFragmentToBookTicketDetailstFragment(location?:"",branchCode?:""))
        }

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observerGetAllBranchesViewModel() {
        branchesViewModel.branchesResponse.observe(viewLifecycleOwner) { branchesList ->
            branchesListAdapter(branchesList)
            val location = branchAdapter?.getTopBranch()?.BranchNameEn
            val branchCode = branchAdapter?.getTopBranch()?.BranchCode.toString()
            binding.nearestBranch.text = location
            binding.nearestBranch.setOnClickListener {
                findNavController().navigate(
                    PayBillBookTicketDirections.actionPayBillBookTicketFragmentToBookTicketDetailstFragment(
                        location ?: "",
                        branchCode ?: ""
                    )
                )
            }
            branchesViewModel.errorResponse.observe(viewLifecycleOwner) {
                Log.v("branch list error", "branch list error")
            }
        }
    }

    private fun branchesListAdapter(branchList : List<BranchResponse>){
        branchAdapter = BranchAdapter(branchList , onItemClick = {
            findNavController().navigate(PayBillBookTicketDirections.actionPayBillBookTicketFragmentToBookTicketDetailstFragment(it.BranchNameEn?:"",it.BranchCode.toString()?:""))
        })

        binding.branchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.branchRecyclerView.adapter = branchAdapter
    }

    private fun callGetAllBranchesApi() {
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.IO){
            val qId = args.qid
            branchesViewModel.getAllBranches(qId)
        }
    }
}