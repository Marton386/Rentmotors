package com.anless.rentmotors.ui.stationInfo

import java.util.*
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.view.ViewGroup
import com.anless.rentmotors.R
import org.osmdroid.util.GeoPoint
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import org.osmdroid.api.IMapController
import androidx.fragment.app.viewModels
import org.osmdroid.views.overlay.Marker
import org.osmdroid.config.Configuration
import androidx.navigation.fragment.navArgs
import com.anless.rentmotors.models.Station
import com.anless.rentmotors.utils.MapUtils
import com.anless.rentmotors.ui.MainActivity
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.common.GoogleApiAvailability
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import com.anless.rentmotors.databinding.FragmentStationInfoBinding


@AndroidEntryPoint
class StationInfoFragment : Fragment(), OnMapReadyCallback {
    private val safeArgs: StationInfoFragmentArgs by navArgs()
    private val viewModel: StationInfoViewModel by viewModels()
    private lateinit var binding: FragmentStationInfoBinding
    private var mMap: GoogleMap? = null
    private lateinit var mapController: IMapController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStationInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if (isDeviceWithGoogleServices()) {
            binding.mapOSM.visibility = View.GONE
        }
        else {
            Configuration.getInstance().userAgentValue = requireContext().packageName
            binding.mapOSM.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            binding.mapOSM.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            binding.mapOSM.setMultiTouchControls(true)
            mapController = binding.mapOSM.controller
            mapController.setZoom(18.0)
        }
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.station.observe(viewLifecycleOwner) { station ->
            (activity as MainActivity).setTitle(station.name)
            if (isDeviceWithGoogleServices()) {
                mMap?.clear()
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(station.location, 15f))
            }
            else {
                binding.mapOSM.overlays.clear()
            }

            addMarker(station)

            binding.tvAddress.text = station.address
            binding.tvPhone.text = station.phone
            binding.tvWorkHours.text = getString(
                R.string.daily_work_hours,
                station.workHourStart,
                station.workHourEnd
            )

            if (station.hasKeyBox) {
                binding.imgReturnKeybox.visibility = View.VISIBLE
                binding.tvReturnKeybox.visibility = View.VISIBLE
            } else {
                binding.imgReturnKeybox.visibility = View.INVISIBLE
                binding.tvReturnKeybox.visibility = View.INVISIBLE
            }

            binding.imgPhone.setOnClickListener {
                binding.imgPhone.isEnabled = false
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${station.phone}")
                startActivity(intent)
                binding.imgPhone.isEnabled = true
            }

            binding.imgNavigation.setOnClickListener {
                val uri: String =
                    java.lang.String.format(
                        Locale.ENGLISH,
                        "geo:%f,%f",
                        station.location.latitude,
                        station.location.longitude
                    )
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                requireContext().startActivity(intent)

            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun addMarker(station: Station) {
        if (isDeviceWithGoogleServices()) {
            val pinBitmapDescriptor = MapUtils.bitmapDescriptorFromVector(requireContext(), R.drawable.ic_marker)
            val markerOptions = MarkerOptions()
                .position(station.location)
                .title(station.name)
                .icon(pinBitmapDescriptor)

            mMap?.addMarker(markerOptions)
        }
        else {
            val myMarker = Marker(binding.mapOSM)
            myMarker.image = resources.getDrawable(R.drawable.ic_marker, null)
            myMarker.title = station.name
            myMarker.icon = resources.getDrawable(R.drawable.ic_marker, null)
            binding.mapOSM.overlays.add(myMarker)
            val point = GeoPoint(station.location.latitude, station.location.longitude)
            myMarker.position = point
            mapController.animateTo(point)
            binding.mapOSM.invalidate()
        }
    }

    private fun isDeviceWithGoogleServices(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        return googleApiAvailability.isGooglePlayServicesAvailable(requireContext()) == ConnectionResult.SUCCESS
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStation(safeArgs.idStation)
    }
}