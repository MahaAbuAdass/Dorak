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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dorak.R
import com.example.dorak.databinding.PayBillFragmentBinding
import com.example.dorak.dataclass.BranchResponse
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.viewmodels.GetAllServicesViewModel
import com.example.dorak.viewmodels.GetBranchesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PayBillFragment : Fragment() {
    private lateinit var binding : PayBillFragmentBinding

    private lateinit var branchesViewModel: GetBranchesViewModel
    var branchAdapter: BranchAdapter? = null

    private val args: PayBillFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=PayBillFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textTitle.text = args.serviceEn

        val getBranchesFactory = GenericViewModelFactory(GetBranchesViewModel::class) {
            GetBranchesViewModel(requireContext())
        }

        branchesViewModel = ViewModelProvider(this, getBranchesFactory).get(GetBranchesViewModel::class.java)

        callGetAllBranchesApi()
        observerGetAllBranchesViewModel()

        binding.cardViewTicket1.setOnClickListener {
            findNavController().navigate(R.id.action_paybill_to_paybillDetails)
        }

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observerGetAllBranchesViewModel() {
        branchesViewModel.branchesResponse.observe(viewLifecycleOwner){branchesList->
            branchesListAdapter(branchesList)
            val location = branchAdapter?.getTopBranch()?.BranchNameEn
            val branchCode = branchAdapter?.getTopBranch()?.BranchCode
            val qId= args.qid
            val serviceEn = args.serviceEn
            val serviceAr= args.serviceAr

            binding.nearestBranch.text = location
            binding.cardViewTicket1.setOnClickListener {
                findNavController().navigate(PayBillFragmentDirections.actionPaybillToPaybillDetails
                    (location?:"",qId?:"",branchCode.toString()?:"",serviceEn?:"",serviceAr?:""))
            }
        }
        branchesViewModel.errorResponse.observe(viewLifecycleOwner){
            Log.v("branch list error", "branch list error")
        }
    }

    private fun branchesListAdapter(branchList : List<BranchResponse>){
        val qId= args.qid


        branchAdapter = BranchAdapter(branchList , onItemClick = {

            val serviceEn = args.serviceEn
            val serviceAr= args.serviceAr
            findNavController().navigate(PayBillFragmentDirections.actionPaybillToPaybillDetails(
                it.BranchNameEn?:"",qId?:"", it.BranchCode.toString()?:"",serviceEn?:"",serviceAr?:""))
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