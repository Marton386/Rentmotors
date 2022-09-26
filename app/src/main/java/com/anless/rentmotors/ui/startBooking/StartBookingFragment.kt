package com.anless.rentmotors.ui.startBooking

import java.util.*
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.view.ViewGroup
import android.app.AlertDialog
import android.widget.TextView
import com.anless.rentmotors.R
import android.view.LayoutInflater
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.fragment.app.Fragment
import com.anless.rentmotors.models.StationType
import androidx.fragment.app.activityViewModels
import com.anless.rentmotors.utils.DateFormatter
import androidx.navigation.fragment.findNavController
import com.anless.rentmotors.databinding.FragmentStartBookingBinding

class StartBookingFragment : Fragment() {
    private val bookingViewModel: BookingViewModel by activityViewModels()
    private lateinit var binding: FragmentStartBookingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBookingBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookingViewModel.clearAll()
        bookingViewModel.initStationPickUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val action =
                StartBookingFragmentDirections.actionStartBookingFragmentToSearchCarsFragment()
            findNavController().navigate(action)
        }

        binding.swSameLocationDropOff.setOnCheckedChangeListener { _, isChecked ->
            binding.layoutDropOffLocation.visibility = if (isChecked) View.GONE else View.VISIBLE
            bookingViewModel.setSameStation(isChecked)
        }

        binding.layoutPickUpLocation.setOnClickListener {
            toSearchStationFragment(StationType.PICK_UP_LOCATION)
        }

        binding.layoutDropOffLocation.setOnClickListener {
            toSearchStationFragment(StationType.DROP_OFF_LOCATION)
        }

        binding.layoutPickUpDate.setOnClickListener {
            pickDate(StationType.PICK_UP_LOCATION)
        }

        binding.layoutDropOffDate.setOnClickListener {
            pickDate(StationType.DROP_OFF_LOCATION)
        }

        binding.tvPickUpTime.setOnClickListener {
            pickTime(StationType.PICK_UP_LOCATION)
        }

        binding.tvDropOffTime.setOnClickListener {
            pickTime(StationType.DROP_OFF_LOCATION)
        }

        subscribeUi()
    }

    private fun toSearchStationFragment(typeStation: Int) {
        val action =
            StartBookingFragmentDirections.actionStartBookingFragmentToSearchStationsFragment(
                typeStation
            )
        findNavController().navigate(action)
    }

    private fun pickDate(stationType: Int) {
        val calendar = getDate(stationType)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // TODO: 17.08.2021 check duplicate
        val dpd = DatePickerDialog(requireContext(), { _, yearOfLife, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, yearOfLife)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            if (stationType == StationType.PICK_UP_LOCATION) {
                bookingViewModel.setDateFrom(calendar)
            } else {
                bookingViewModel.setDateTo(calendar)
            }
        }, year, month, day)
        dpd.datePicker.minDate = bookingViewModel.getMinDateToInMillis(stationType)
        if (stationType == StationType.DROP_OFF_LOCATION) {
            calendar.add(Calendar.YEAR, 1)
            dpd.datePicker.maxDate = calendar.timeInMillis
        }
        // TODO: 11.08.2021 max date
        dpd.show()
    }

    private fun pickTime(stationType: Int) {
        val calendar = getDate(stationType)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(requireContext(), { _, hourOfDay, minuteOfHour ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minuteOfHour)
            if (stationType == StationType.PICK_UP_LOCATION) {
                if (calendar.timeInMillis >= bookingViewModel.getMinDateToInMillis(stationType)) {
                    bookingViewModel.setDateFrom(calendar)
                } else {
                    bookingViewModel.updateMinDateFrom()
                    showError(R.string.date_too_early)
                }
            } else {
                bookingViewModel.setDateTo(calendar)
            }
        }, hour, minute, true)
        tpd.show()
    }

    private fun getDate(stationType: Int): Calendar {
        return if (stationType == StationType.PICK_UP_LOCATION)
            bookingViewModel.dateFrom.value!!
        else
            bookingViewModel.dateTo.value!!
    }

    private fun subscribeUi() {
        bookingViewModel.dateFrom.observe(viewLifecycleOwner) {
            if (it != null) {
                updateDateFrom(it)
            }
        }

        bookingViewModel.dateTo.observe(viewLifecycleOwner) {
            if (it != null) {
                updateDateTo(it)
            }
        }

        bookingViewModel.stationPickUp.observe(viewLifecycleOwner) {
            binding.tvPickUpLocation.text = it.name
        }

        bookingViewModel.stationDropOff.observe(viewLifecycleOwner) {
            binding.tvDropOffLocation.text = it.name
        }

        bookingViewModel.searchCarFilter.observe(viewLifecycleOwner) {
            binding.btnNext.isEnabled = it != null
        }

        bookingViewModel.isLoading.observe(viewLifecycleOwner) {
            with(binding.swipeRefresh) {
                isRefreshing = it
                isEnabled = it
                isRefreshing = it
            }
        }
    }

    private fun updateDateFrom(calendar: Calendar) {
        binding.tvPickUpDate.text = DateFormatter.formatRentDate(calendar)
        binding.tvPickUpDayOfWeek.text = DateFormatter.formatRentDayOfWeek(calendar)
        binding.tvPickUpTime.text = DateFormatter.formatRentTime(calendar)
    }

    private fun updateDateTo(calendar: Calendar) {
        binding.tvDropOffDate.text = DateFormatter.formatRentDate(calendar)
        binding.tvDropOffDayOfWeek.text = DateFormatter.formatRentDayOfWeek(calendar)
        binding.tvDropOffTime.text = DateFormatter.formatRentTime(calendar)
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

    override fun onResume() {
        super.onResume()
        bookingViewModel.checkDateFrom()
    }
}