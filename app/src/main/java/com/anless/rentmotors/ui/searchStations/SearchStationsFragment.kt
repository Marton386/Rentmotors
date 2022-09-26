package com.anless.rentmotors.ui.searchStations

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.anless.rentmotors.R
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.anless.rentmotors.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import com.anless.rentmotors.models.StationType
import com.anless.rentmotors.utils.AndroidUtils
import androidx.fragment.app.activityViewModels
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anless.rentmotors.ui.startBooking.BookingViewModel
import com.anless.rentmotors.databinding.FragmentSearchStationBinding

@AndroidEntryPoint
class SearchStationsFragment : Fragment() {
    private val safeArgs: SearchStationsFragmentArgs by navArgs()
    private val bookingViewModel: BookingViewModel by activityViewModels()
    private val viewModel: SearchStationsViewModel by viewModels()
    private lateinit var binding: FragmentSearchStationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchStationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTypeLocation.text =
            if (safeArgs.typeStation == StationType.PICK_UP_LOCATION) getString(
                R.string.car_pickup_location
            ) else getString(
                R.string.car_dropOff_location
            )

        AndroidUtils.openKeyboard(binding.etLocation)

        (activity as MainActivity).setTitle(getString(R.string.choose_station))

        val adapter = SearchStationAdapter {
            if (safeArgs.typeStation == StationType.PICK_UP_LOCATION) {
                bookingViewModel.setStationPickUp(it)
            } else {
                bookingViewModel.setStationDropOff(it)
            }
            findNavController().popBackStack()
        }

        val layoutManager = LinearLayoutManager(requireContext())

        with(binding.rvSearchStations) {
            this.layoutManager = layoutManager
            this.adapter = adapter
        }

        binding.etLocation.addTextChangedListener {
            if (it.toString().trim() == "") {
                binding.imgSearch.visibility = View.VISIBLE
                binding.imgCancel.visibility = View.GONE
            } else {
                binding.imgSearch.visibility = View.GONE
                binding.imgCancel.visibility = View.VISIBLE
            }
            viewModel.setFilter(it.toString().trim())
        }

        binding.imgCancel.setOnClickListener {
            binding.etLocation.setText("")
        }

        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: SearchStationAdapter) {
        viewModel.filteredStations.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}