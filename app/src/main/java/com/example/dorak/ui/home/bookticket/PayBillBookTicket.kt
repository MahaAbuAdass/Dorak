package com.example.dorak.ui.home.bookticket

import android.location.Location
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

        binding.title.text = args.serviceEn


        val getBranchesFactory = GenericViewModelFactory(GetBranchesViewModel::class) {
            GetBranchesViewModel(requireContext())
        }

        branchesViewModel = ViewModelProvider(this, getBranchesFactory).get(GetBranchesViewModel::class.java)

        callGetAllBranchesApi()
        observerGetAllBranchesViewModel()
/*
       binding.cardViewTicket1.setOnClickListener {
           val location = branchAdapter?.getTopBranch()?.BranchNameEn
            val branchCode = branchAdapter?.getTopBranch()?.BranchCode.toString()
            val qID= args.qid
            val serviceEn = args.serviceEn
            val serviceAr = args.serviceAr

            findNavController().navigate(PayBillBookTicketDirections.actionPayBillBookTicketFragmentToBookTicketDetailstFragment(
                location?:"",branchCode?:"",qID,serviceEn,serviceAr))
        }
*/
        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observerGetAllBranchesViewModel() {
        branchesViewModel.branchesResponse.observe(viewLifecycleOwner) { branchesList ->
            branchesListAdapter(branchesList)

            // Reference location (Fixed point to compare with)
            val referenceLat = 32.564473459145525
            val referenceLng = 35.85526088115223


            // Find nearest branch
            val nearestBranch = branchesList.minByOrNull { branch ->
                val branchLat = branch.latitude?.toDoubleOrNull() ?: 0.0
                val branchLng = branch.longitude?.toDoubleOrNull() ?: 0.0
                calculateDistance(referenceLat, referenceLng, branchLat, branchLng)
            }

            if (nearestBranch != null) {
                val location = nearestBranch.BranchNameEn
                val branchCode = nearestBranch.BranchCode
                val qId = args.qid
                val serviceEn = args.serviceEn
                val serviceAr = args.serviceAr



                binding.nearestBranch.text = location
                binding.cardViewTicket1.setOnClickListener {
                    findNavController().navigate(PayBillBookTicketDirections.actionPayBillBookTicketFragmentToBookTicketDetailstFragment(
                            location ?: "",
                            qId ?: "",
                            branchCode.toString() ?: "",
                            serviceEn ?: "",
                            serviceAr ?: ""
                        )
                    )
                    Log.v("branch list error", location + qId+ branchCode.toString() + serviceEn + serviceAr)

                }
            }
        }

        branchesViewModel.errorResponse.observe(viewLifecycleOwner) {
            Log.v("branch list error", "branch list error")
        }
    }


    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] // Distance in meters
    }

    private fun branchesListAdapter(branchList : List<BranchResponse>){
        branchAdapter = BranchAdapter(branchList , onItemClick = {
            val qID= args.qid
            val serviceEn = args.serviceEn
            val serviceAr = args.serviceAr

            findNavController().navigate(PayBillBookTicketDirections.actionPayBillBookTicketFragmentToBookTicketDetailstFragment(

                it.BranchNameEn?:"",it.BranchCode.toString()?:"" , qID,serviceEn,serviceAr))
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