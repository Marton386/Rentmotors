package com.anless.rentmotors.ui.searchCars

import java.util.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Button
import android.view.ViewGroup
import android.widget.TextView
import android.app.AlertDialog
import com.anless.rentmotors.R
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anless.rentmotors.models.Car
import dagger.hilt.android.AndroidEntryPoint
import com.anless.rentmotors.ui.MainActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anless.rentmotors.ui.startBooking.BookingViewModel
import com.anless.rentmotors.databinding.FragmentSearchCarsBinding

@AndroidEntryPoint
class SearchCarsFragment : Fragment() {
    private val bookingViewModel: BookingViewModel by activityViewModels()
    private val viewModel: SearchCarsViewModel by viewModels()
    private lateinit var binding: FragmentSearchCarsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchCarsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val glide = Glide.with(requireContext())
        val adapter = CarsAdapter(glide, object : CarsAdapter.CarItemListener {
            override fun onItemClick(car: Car) {
                bookingViewModel.setCar(car)
                val action =
                    SearchCarsFragmentDirections.actionSearchCarsFragmentToBookingDetailsFragment()
                findNavController().navigate(action)
            }

            override fun onRequestInfoClick(car: Car) {
                showToast(R.string.car_on_request)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())

        with(binding.rvCars) {
            this.layoutManager = layoutManager
            this.adapter = adapter
            addItemDecoration(CarMarginItemDecoration(requireContext()))
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadCars()
        }

        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: CarsAdapter) {
        bookingViewModel.searchCarFilter.observe(viewLifecycleOwner) {
            viewModel.setCarFilter(it)
        }

        viewModel.searchCarsWrapper.observe(viewLifecycleOwner) {
            if (it.cars.isNotEmpty()) {
                adapter.submitList(it.cars)
                bookingViewModel.setDays(it.days)
                bookingViewModel.setCurrency(it.currency)
            } else {
                showError(R.string.no_available_cars)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }

        bookingViewModel.stationPickUp.observe(viewLifecycleOwner) {
            val title = "${it.name} (${it.shortCode.uppercase(Locale.getDefault())})"
            (activity as MainActivity).setTitle(title)
        }

        bookingViewModel.rentalDateString.observe(viewLifecycleOwner) {
            (activity as MainActivity).setSubTitle(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            showError(it)
        }
    }

    private fun showToast(message: Int) {
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(requireContext(), getString(message), duration)
        toast.show()
    }

    private fun showError(error: Int) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_message)

        val dialog = alertDialogBuilder.show()

        dialog.findViewById<TextView>(R.id.tvMessage).setText(error)
        dialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onDestroyView() {
        (activity as MainActivity).setSubTitle(null)
        super.onDestroyView()
    }
}