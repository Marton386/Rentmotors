package com.anless.rentmotors.ui.stationsMap

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.anless.rentmotors.R
import org.osmdroid.util.GeoPoint
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import org.osmdroid.api.IMapController
import androidx.fragment.app.viewModels
import org.osmdroid.config.Configuration
import org.osmdroid.views.overlay.Marker
import com.anless.rentmotors.models.Station
import com.anless.rentmotors.utils.MapUtils
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.common.GoogleApiAvailability
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import com.anless.rentmotors.databinding.FragmentStationsMapBinding


@AndroidEntryPoint
class StationsMapFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: StationsMapViewModel by viewModels()
    private lateinit var binding: FragmentStationsMapBinding
    private var mMap: GoogleMap? = null
    private lateinit var mapController: IMapController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStationsMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if (isDeviceWithGoogleServices()) {
            binding.mapOSM.visibility = View.GONE
        } else {
            mapFragment.view?.visibility = View.GONE
            Configuration.getInstance().userAgentValue = requireContext().packageName
            binding.mapOSM.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            binding.mapOSM.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            binding.mapOSM.setMultiTouchControls(true)
            mapController = binding.mapOSM.controller
            mapController.setZoom(10.0)
        }
        subscribeUi()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.setOnMarkerClickListener {
            val idStation = it.tag as Int
            val action =
                StationsMapFragmentDirections.actionStationsMapFragmentToStationInfoFragment(
                    idStation
                )
            findNavController().navigate(action)
            true
        }
    }

    private fun subscribeUi() {
        viewModel.stations.observe(viewLifecycleOwner) {
            reconfigureMap(it)
        }
    }

    private fun reconfigureMap(stations: List<Station>) {
        if (isDeviceWithGoogleServices())
            mMap?.clear()
        else
            binding.mapOSM.overlays.clear()

        addMarkers(stations)
        var movePosition: LatLng? = null
        for (station in stations) {
            if (station.id == Station.MAIN_STATION_ID) {
                movePosition = station.location
            }
        }

        if (movePosition != null) {
            /*
            get user location permission and show nearby station
            if not allowed - show main station
             */
            if (isDeviceWithGoogleServices()) {
                val cu = CameraUpdateFactory.newLatLngZoom(movePosition, 7f)
                mMap?.moveCamera(cu)
            }
            else {
                val point = GeoPoint(movePosition.latitude, movePosition.longitude)
                mapController.animateTo(point)
            }
        }
    }

    private fun addMarkers(stations: List<Station>) {
        if (isDeviceWithGoogleServices()) {
            val pinBitmapDescriptor =
                MapUtils.bitmapDescriptorFromVector(requireContext(), R.drawable.ic_marker)
            for (station in stations) {
                val markerOptions = MarkerOptions()
                    .position(station.location)
                    .title(station.name)
                    .icon(pinBitmapDescriptor)
                val marker = mMap?.addMarker(markerOptions)
                marker?.tag = station.id
            }
        }
        else {
            for (station in stations) {
                val myMarker = Marker(binding.mapOSM)
                myMarker.image = resources.getDrawable(R.drawable.ic_marker, null)
                myMarker.title = station.name
                myMarker.icon = resources.getDrawable(R.drawable.ic_marker, null)
                binding.mapOSM.overlays.add(myMarker)
                val point = GeoPoint(station.location.latitude, station.location.longitude)
                myMarker.position = point
                binding.mapOSM.invalidate()
            }
        }
    }

    private fun isDeviceWithGoogleServices(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        return googleApiAvailability.isGooglePlayServicesAvailable(requireContext()) == ConnectionResult.SUCCESS
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadStations()
    }
}