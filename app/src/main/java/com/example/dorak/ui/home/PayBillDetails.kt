package com.example.dorak.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dorak.R
import com.example.dorak.databinding.PayBillDetailsBinding
import com.example.dorak.util.PreferenceManager
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*

class PayBillDetails : Fragment() {

    private lateinit var binding: PayBillDetailsBinding
    private val args: PayBillDetailsArgs by navArgs()

    private lateinit var mapView: MapView
    private lateinit var pointAnnotationManager: PointAnnotationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PayBillDetailsBinding.inflate(inflater, container, false)
        mapView = binding.mapView

        // Load the map style and set camera position
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {


            val location = Point.fromLngLat(35.859662131006196, 32.565937781270584)

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

        binding.title.text = args.location
        binding.details.text = args.location
        binding.address.text = args.location

        val phone = PreferenceManager.getMobile(requireContext())
        binding.phone.text = phone

        binding.btnGenerateTicket.setOnClickListener {
            findNavController().navigate(R.id.action_paybillDetails_to_ticketDetails)
        }

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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
