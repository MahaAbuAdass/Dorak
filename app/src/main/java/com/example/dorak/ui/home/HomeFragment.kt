package com.example.dorak.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

