package com.example.dorak.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PayBillFragment : Fragment() {
    private lateinit var binding : PayBillFragmentBinding

    private lateinit var branchesViewModel: GetBranchesViewModel
    var branchAdapter: BranchAdapter? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double? = null
    private var longitude: Double? = null

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Check permission and get current location
        if (checkLocationPermission()) {
            getLastKnownLocation()
        } else {
            requestLocationPermission()
        }

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

    @SuppressLint("MissingPermission") // Assuming permission is granted
    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
                Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
            } else {
                Log.e("Location", "Last known location is null")
            }
        }.addOnFailureListener {
            Log.e("Location", "Error getting location: ${it.message}")
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        // Request permission if not granted
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )
    }



private fun observerGetAllBranchesViewModel() {
        branchesViewModel.branchesResponse.observe(viewLifecycleOwner) { branchesList ->
            branchesListAdapter(branchesList)

            // Reference location (Fixed point to compare with)
            val referenceLat = 32.564430947987574
            val referenceLng = 35.855123465677664

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
                    findNavController().navigate(
                        PayBillFragmentDirections.actionPaybillToPaybillDetails(
                            location ?: "",
                            qId ?: "",
                            branchCode.toString() ?: "",
                            serviceEn ?: "",
                            serviceAr ?: ""
                        )
                    )
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