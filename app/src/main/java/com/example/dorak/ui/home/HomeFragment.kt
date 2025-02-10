package com.example.dorak.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.dorak.R
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.dorak.databinding.FragmentHomeBinding
import com.example.dorak.dataclass.MyTicketResponse
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.ui.myticket.MyTicketAdapter
import com.example.dorak.util.PreferenceManager
import com.example.dorak.viewmodels.MyTicketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var myTicketViewModel : MyTicketViewModel
    var myTicketAdapter: MyTicketAdapterHome? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myTicketFactory = GenericViewModelFactory(MyTicketViewModel::class) {
            MyTicketViewModel(requireContext())
        }

        myTicketViewModel = ViewModelProvider(this, myTicketFactory).get(
            MyTicketViewModel::class.java)

        val username = PreferenceManager.getUsername(requireContext())
        binding.name.text = username

        viewPager = view.findViewById(R.id.bannerViewPager)

        // List of image resources for the banner
        val imageList = listOf(
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
        )

        val adapter = BannerAdapter(imageList)
        viewPager.adapter = adapter

        // Prevent clipping so we can see part of next/previous images
// Prevent clipping
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3

// Page transformer for scaling effect
        viewPager.setPageTransformer { page, position ->
            val scaleFactor = 0.85f + (1 - kotlin.math.abs(position)) * 0.15f
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
        }

        // Set the custom PageTransformer to show part of next and previous images
        viewPager.setPageTransformer(BannerPageTransformer())
        // Set up the listener for BottomNavigationView



        binding.cardviewGetticket.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreen_to_getTicket)
        }

        binding.cardviewBookTicket.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreen_to_bookTicketFragment)
        }

        callMyTicketApi()
        observerMyTicketViewModel()
        checkLocationPermission()

    }

    private fun checkLocationPermission() {
        when {
            // If permission is already granted
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
             //   Toast.makeText(requireContext(), "Location permission already granted!", Toast.LENGTH_SHORT).show()
            }

            // If permission is not granted, request it
            else -> {
                requestLocationPermissionLauncher.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
        }
    }

    // Request location permissions
    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted || coarseLocationGranted) {
                Toast.makeText(requireContext(), "Location permission granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Location permission denied!", Toast.LENGTH_SHORT).show()
            }
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
            Log.v("error response",it.toString())
        }
    }

    private fun myTicketListAdapter(myTicket : List<MyTicketResponse>){
        myTicketAdapter = MyTicketAdapterHome(myTicket , onItemClick = {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavMyTicket())
        } )
        binding.myTicketRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.myTicketRecyclerView.adapter = myTicketAdapter
    }


}

