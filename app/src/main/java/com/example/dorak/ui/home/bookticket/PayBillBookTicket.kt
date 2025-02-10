package com.example.dorak.ui.home.bookticket

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.example.dorak.ui.home.PayBillFragmentDirections
import com.example.dorak.viewmodels.GetBranchesViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PayBillBookTicket : Fragment() {

    private lateinit var binding: PayBillBookTicketBinding
    private lateinit var branchesViewModel: GetBranchesViewModel
    private var branchAdapter: BranchAdapter? = null

    private val args: PayBillBookTicketArgs by navArgs()
    private var latitude: Double? = null
    private var longitude: Double? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PayBillBookTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = args.serviceEn

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Directly get location since permission was already checked
        getLastKnownLocation()

        val getBranchesFactory = GenericViewModelFactory(GetBranchesViewModel::class) {
            GetBranchesViewModel(requireContext())
        }

        branchesViewModel = ViewModelProvider(this, getBranchesFactory).get(GetBranchesViewModel::class.java)

        callGetAllBranchesApi()
        observerGetAllBranchesViewModel()

        binding.cardViewTicket1.setOnClickListener {
            val location = branchAdapter?.getTopBranch()?.BranchNameEn
            val branchCode = branchAdapter?.getTopBranch()?.BranchCode.toString()
            val qID = args.qid
            val serviceEn = args.serviceEn
            val serviceAr = args.serviceAr

            findNavController().navigate(
                PayBillBookTicketDirections.actionPayBillBookTicketFragmentToBookTicketDetailstFragment(
                    location ?: "", branchCode ?: "", qID, serviceEn, serviceAr
                )
            )
        }

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    @SuppressLint("MissingPermission") // Permission assumed to be granted earlier
    private fun getLastKnownLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                        Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
                    } else {
                        Log.e("Location", "Last known location is null")
                        getCurrentLocation()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Location", "Error getting location: ${exception.message}")
                }
        } else {
            Log.e("Location", "Permission not granted")
        }
    }

    private fun getCurrentLocation() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10 seconds
            fastestInterval = 5000 // 5 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
                    fusedLocationClient.removeLocationUpdates(this) // Stop updates once we have a location
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun observerGetAllBranchesViewModel() {
        // Observe branches response
        branchesViewModel.branchesResponse.observe(viewLifecycleOwner) { branchesList ->
            branchesListAdapter(branchesList)

            val referenceLat = latitude
            val referenceLng = longitude

            // Find nearest branch
            val nearestBranch = branchesList.minByOrNull { branch ->
                val branchLat = branch.latitude?.toDoubleOrNull() ?: 0.0
                val branchLng = branch.longitude?.toDoubleOrNull() ?: 0.0
                calculateDistance(referenceLat ?: 0.0, referenceLng ?: 0.0, branchLat, branchLng)
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

    private fun branchesListAdapter(branchList: List<BranchResponse>) {
        branchAdapter = BranchAdapter(branchList, onItemClick = {
            val qID = args.qid
            val serviceEn = args.serviceEn
            val serviceAr = args.serviceAr

            findNavController().navigate(
                PayBillBookTicketDirections.actionPayBillBookTicketFragmentToBookTicketDetailstFragment(
                    it.BranchNameEn ?: "", it.BranchCode.toString() ?: "", qID, serviceEn, serviceAr
                )
            )
        })

        binding.branchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.branchRecyclerView.adapter = branchAdapter
    }

    private fun callGetAllBranchesApi() {
        lifecycleScope.launch(Dispatchers.IO) {
            val qId = args.qid
            branchesViewModel.getAllBranches(qId)
        }
    }
}
