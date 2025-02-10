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
import com.example.dorak.R
import com.example.dorak.databinding.PayBillDetailsBinding
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.util.PreferenceManager
import com.example.dorak.viewmodels.GenerateTicketViewModel
import com.example.dorak.viewmodels.GetWaitingCountViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PayBillDetails : Fragment() {

    private lateinit var binding: PayBillDetailsBinding
    private val args: PayBillDetailsArgs by navArgs()

    private lateinit var mapView: MapView
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private lateinit var getWaitingCountViewModel : GetWaitingCountViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PayBillDetailsBinding.inflate(inflater, container, false)
        mapView = binding.mapView

        // Load the map style and set camera position
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {


            val location = Point.fromLngLat(35.85512609609165, 32.56442604997418)

            // Move the camera to the specific location
            mapView.getMapboxMap().setCamera(
                CameraOptions.Builder()
                    .center(location)
                    .zoom(15.0)  // Adjusted zoom level to focus on the location
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


        binding.title.text = args.location
        binding.details.text = args.location
        //binding.address.text = args.location

        binding.selectService.text = args.serviceEn


        val qId= args.qid
        val branchCode = args.branchCode
        val phone = PreferenceManager.getMobile(requireContext())
       // binding.phone.text = phone

        val serviceEn = args.serviceEn
        val serviceAr= args.serviceAr

        binding.btnGenerateTicket.setOnClickListener {
            findNavController().navigate(PayBillDetailsDirections.actionPaybillDetailsToTicketDetails(qId?:"",branchCode?:"",serviceEn?:"",serviceAr?:""))
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
