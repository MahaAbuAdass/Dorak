package com.example.dorak.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dorak.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.dorak.databinding.FragmentHomeBinding
import com.example.dorak.ui.home.BannerAdapter
import com.example.dorak.ui.home.BannerPageTransformer

class HomeFragment : Fragment() {

    private lateinit var bottomNavigationView: BottomNavigationView
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

        // Initialize BottomNavigationView
      //  bottomNavigationView = view.findViewById(R.id.bottomNavigationView)


        viewPager = view.findViewById(R.id.bannerViewPager)

        // List of image resources for the banner
        val imageList = listOf(
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
        )

        val adapter = BannerAdapter(imageList)
        viewPager.adapter = adapter

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

