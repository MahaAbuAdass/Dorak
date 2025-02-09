package com.example.dorak.ui.home.bookticket

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dorak.databinding.BranchDetailsBookTicketBinding
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.ui.home.PayBillDetailsDirections
import com.example.dorak.util.PreferenceManager
import com.example.dorak.viewmodels.BookTicketViewModel
import com.example.dorak.viewmodels.GenerateTicketViewModel
import com.example.dorak.viewmodels.GetWaitingCountViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BranchDetailsBookTicket : Fragment() {

    private lateinit var binding : BranchDetailsBookTicketBinding

    private val args : BranchDetailsBookTicketArgs by navArgs()
    private lateinit var bookTicketViewModel : BookTicketViewModel
    private lateinit var mapView: MapView
    private lateinit var getWaitingCountViewModel : GetWaitingCountViewModel
    private lateinit var pointAnnotationManager: PointAnnotationManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=BranchDetailsBookTicketBinding.inflate(inflater,container,false)
        mapView = binding.mapView

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {


            val location = Point.fromLngLat(35.85533188547881, 32.5645377661438)

            // Move the camera to the specific location
            mapView.getMapboxMap().setCamera(
                CameraOptions.Builder()
                    .center(location)
                    .zoom(14.0)  // Adjusted zoom level to focus on the location
                    .pitch(0.0)
                    .bearing(0.0)
                    .build()
            )

            // Add a marker to the map
            addMarker(location)
        }

        return binding.root
    }

    private fun addMarker(location: Point) {
        val annotationApi = mapView.annotations
        pointAnnotationManager = annotationApi.createPointAnnotationManager()

        val pointAnnotationOptions = PointAnnotationOptions()
            .withPoint(location)

        pointAnnotationManager.create(pointAnnotationOptions)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getWaitingTimeFactory = GenericViewModelFactory(GetWaitingCountViewModel::class) {
            GetWaitingCountViewModel(requireContext())
        }

        getWaitingCountViewModel =
            ViewModelProvider(this, getWaitingTimeFactory).get(GetWaitingCountViewModel::class.java)


        val bookTicketFactory = GenericViewModelFactory(BookTicketViewModel::class) {
            BookTicketViewModel(requireContext())
        }

        bookTicketViewModel = ViewModelProvider(this, bookTicketFactory).get(
            BookTicketViewModel::class.java)

        val date = args.date
        val time = args.time
        val location = args.location
        val serviceEn = args.serviceEn
        val serviceAr = args.serviceAr
        val userId= PreferenceManager.getUserId(requireContext())

        binding.title.text = location
        binding.details.text = location
        binding.address.text = location

        binding.selectService.text = serviceEn
        binding.date.text = date
        binding.ticketTime.text = time


        val qId= args.qid
        val branchCode = args.branchCode

        val phone = PreferenceManager.getMobile(requireContext())
        //binding.phone.text = phone



        binding.btnGenerateTicket.setOnClickListener {
            callBookTicketApi(date,qId,location,userId?:"",time)
            observerBookTicketApi()

        //    findNavController().navigate(PayBillDetailsDirections.actionPaybillDetailsToTicketDetails(qId?:"",branchCode?:"",serviceEn?:"",serviceAr?:""))
        }

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        callWaitingCountApi(branchCode,qId)
        observerWaitingCountViewModel()
    }

    private fun observerWaitingCountViewModel() {
        getWaitingCountViewModel.getWaitingCountResponse.observe(viewLifecycleOwner){
            binding.tvEstimatedCount.text = it.avgWaitedMinutes
            binding.tvWaitingCount.text = it.WaitingCount

        }

        getWaitingCountViewModel.errorResponse.observe(viewLifecycleOwner){
            Log.v("error generating the ticket", it.toString())
        }

    }

    private fun callWaitingCountApi(branchCode : String ,qID: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            getWaitingCountViewModel.getWaitingCount(branchCode, qID)
        }
    }

    private fun observerBookTicketApi() {
        bookTicketViewModel.bookTicketResponse.observe(viewLifecycleOwner){bookTicket->
            if (bookTicket.MsgStr == "This User has an appointment already for this date") {
                Toast.makeText(requireContext(), bookTicket.MsgStr , Toast.LENGTH_SHORT).show()
            }
            else{
                findNavController().navigate(BranchDetailsBookTicketDirections.actionBranchDetailsBookTicketFragmentToTicketDetailsBookTicketFragment())
            }

            }


        bookTicketViewModel.errorResponse.observe(viewLifecycleOwner){
            Log.v("error generating the ticket",it.toString())

        }

    }

    private fun callBookTicketApi(apptDate: String,qID: String,branchCode: String,user_id: String ,app_time: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            bookTicketViewModel.bookTicket(apptDate, qID, branchCode, user_id, app_time)
        }
    }

    @SuppressLint("Lifecycle")
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}