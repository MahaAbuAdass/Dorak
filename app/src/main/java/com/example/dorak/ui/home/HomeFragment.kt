package com.example.dorak.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.dorak.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.dorak.databinding.FragmentHomeBinding
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.ui.home.BannerAdapter
import com.example.dorak.ui.home.BannerPageTransformer
import com.example.dorak.ui.login.LoginViewModel
import com.example.dorak.ui.login.services.GetAllServicesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewPager: ViewPager2


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


    }




}

