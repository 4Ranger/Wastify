package com.capstonewahwah.wastify.ui.main.location

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.Fade
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.data.local.PengepulLocation
import com.capstonewahwah.wastify.databinding.FragmentLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding

    private lateinit var mMap: GoogleMap
    private val boundsBuilder = LatLngBounds.Builder()

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.setOnMarkerClickListener(onMarkerClickListener)
        addPengepulLocation()
    }

    private val onMarkerClickListener = GoogleMap.OnMarkerClickListener { marker ->
        marker.showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f))
        true
    }

    private fun addPengepulLocation() {
        val pengepulLocation = listOf(
            PengepulLocation("Wastify 1", "Buka di jam 6-8", 3.547111, 98.655228),
            PengepulLocation("Wastify 2", "Buka di jam 6-8", 3.592123, 98.731392),
            PengepulLocation("Wastify 3", "Buka di jam 6-8", 3.577783, 98.653533),
            PengepulLocation("Wastify 4", "Buka di jam 6-8", 3.524382, 98.658774),
            PengepulLocation("Wastify 5", "Buka di jam 6-8", 3.628427, 98.671026),
            PengepulLocation("Wastify 6", "Buka di jam 6-8", -7.273057, 112.792000),
        )
        pengepulLocation.forEach { pengepul ->
            val latLng = LatLng(pengepul.latitude, pengepul.longitude)
            mMap.addMarker(MarkerOptions().position(latLng).title(pengepul.name).snippet(pengepul.details))
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade()
        exitTransition = Fade()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}