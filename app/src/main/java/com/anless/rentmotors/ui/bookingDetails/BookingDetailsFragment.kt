package com.anless.rentmotors.ui.bookingDetails

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.anless.rentmotors.R
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.anless.rentmotors.models.Fee
import com.anless.rentmotors.models.Extra
import androidx.fragment.app.activityViewModels
import com.anless.rentmotors.utils.DateFormatter
import com.anless.rentmotors.utils.PriceFormatter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anless.rentmotors.databinding.ItemFeeBinding
import com.anless.rentmotors.ui.startBooking.BookingViewModel
import com.anless.rentmotors.databinding.FragmentBookingDetailsBinding

class BookingDetailsFragment : Fragment() {
    private val bookingViewModel: BookingViewModel by activityViewModels()
    private lateinit var binding: FragmentBookingDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinueBooking.setOnClickListener {
            val action =
                BookingDetailsFragmentDirections.actionBookingDetailsFragmentToCheckClientFragment()
            findNavController().navigate(action)

        }

        binding.ibMoreCarInfo.setOnClickListener {
            binding.ibMoreCarInfo.visibility = View.GONE
            binding.ibHideCarInfo.visibility = View.VISIBLE
            binding.layoutCarExtraInfo.visibility = View.VISIBLE
        }

        binding.ibHideCarInfo.setOnClickListener {
            binding.ibMoreCarInfo.visibility = View.VISIBLE
            binding.ibHideCarInfo.visibility = View.INVISIBLE
            binding.layoutCarExtraInfo.visibility = View.GONE
        }

        binding.ibMorePickUpDropOffInfo.setOnClickListener {
            binding.ibMorePickUpDropOffInfo.visibility = View.INVISIBLE
            binding.ibHidePickUpDropOffInfo.visibility = View.VISIBLE
            binding.layoutPickUpDropOffInfo.visibility = View.VISIBLE
        }

        binding.ibHidePickUpDropOffInfo.setOnClickListener {
            binding.ibHidePickUpDropOffInfo.visibility = View.INVISIBLE
            binding.ibMorePickUpDropOffInfo.visibility = View.VISIBLE
            binding.layoutPickUpDropOffInfo.visibility = View.GONE
        }

        binding.ibMoreExtrasInfo.setOnClickListener {
            binding.ibMoreExtrasInfo.visibility = View.INVISIBLE
            binding.ibHideExtrasInfo.visibility = View.VISIBLE
            binding.scrollView.visibility = View.VISIBLE
        }

        binding.ibHideExtrasInfo.setOnClickListener {
            binding.ibHideExtrasInfo.visibility = View.INVISIBLE
            binding.ibMoreExtrasInfo.visibility = View.VISIBLE
            binding.scrollView.visibility = View.GONE
        }

        binding.ibHideFeesInfo.setOnClickListener {
            binding.ibHideFeesInfo.visibility = View.INVISIBLE
            binding.ibMoreFeesInfo.visibility = View.VISIBLE
            binding.layoutFeesList.visibility = View.GONE
        }

        binding.ibMoreFeesInfo.setOnClickListener {
            binding.ibHideFeesInfo.visibility = View.VISIBLE
            binding.ibMoreFeesInfo.visibility = View.INVISIBLE
            binding.layoutFeesList.visibility = View.VISIBLE
        }

        val adapter = ExtrasAdapter(object : ExtrasAdapter.OnEditExtra {
            override fun onExtraClick(extra: Extra) {
                bookingViewModel.editExtra(extra)
            }

            override fun onAddressChanged(extra: Extra, address: String) {
                bookingViewModel.setExtraAddress(extra, address)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())

        with(binding.rvExtras) {
            this.layoutManager = layoutManager
            this.adapter = adapter
        }

        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: ExtrasAdapter) {
        bookingViewModel.car.observe(viewLifecycleOwner) {
            binding.tvCarModel.text = it.model
            binding.tvPerDayCost.text = "${PriceFormatter.format(it.price)} ${bookingViewModel.getCurrency()}"
            binding.tvDeposit.text = "${PriceFormatter.format(it.deposit)} ${bookingViewModel.getCurrency()}"
            binding.tvCarClass.text = it.code
            binding.tvAmountPlaces.text = it.seats.toString()
            binding.tvAmountDoors.text = it.doors.toString()
            binding.tvAmountBags.text = it.bag.toString()
            binding.tvAmountLuggage.text = it.luggage.toString()
            binding.tvPerDayCost.text = "${PriceFormatter.format(it.perDayPrice)} ${bookingViewModel.getCurrency()}"
            binding.tvAllDayCostCur.text = bookingViewModel.getCurrency()

            var facilities =
                if (it.at) requireContext().getString(R.string.type_at) else requireContext().getString(
                    R.string.type_mt
                )

            if (it.ac) {
                facilities += ", ${requireContext().getString(R.string.conditioner)}"
            }

            binding.tvFacilities.text = facilities

            Glide
                .with(requireContext())
                .load(it.urlImg)
                .into(binding.imgCar)

            if (it.fees.isEmpty()) {
                binding.layoutFees.visibility = View.GONE
            } else {
                renderFees(it.fees)
                binding.layoutFees.visibility = View.VISIBLE
            }
        }

        bookingViewModel.stationPickUp.observe(viewLifecycleOwner) {
            binding.tvPickUpLocation.text = it.name
        }

        bookingViewModel.stationDropOff.observe(viewLifecycleOwner) {
            binding.tvDropOffLocation.text = it.name
        }

        bookingViewModel.dateFrom.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvPickUpDate.text = DateFormatter.formatRentDate(it)
                binding.tvPickUpTime.text = DateFormatter.formatRentTime(it)
                binding.tvPickUpDayOfWeek.text = DateFormatter.formatRentDayOfWeek(it)
            }
        }

        bookingViewModel.dateTo.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvDropOffDate.text = DateFormatter.formatRentDate(it)
                binding.tvDropOffTime.text = DateFormatter.formatRentTime(it)
                binding.tvDropOffDayOfWeek.text = DateFormatter.formatRentDayOfWeek(it)
            }
        }

        bookingViewModel.price.observe(viewLifecycleOwner) {
            binding.tvAllDayCost.text = PriceFormatter.format(it)
        }

        bookingViewModel.limitedMileage.observe(viewLifecycleOwner) {
            binding.tvMileageLimit.text = requireContext().getString(R.string.km, it)
        }

        bookingViewModel.selectedExtras.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        bookingViewModel.days.observe(viewLifecycleOwner) {
            binding.tvForDays.text = resources.getQuantityString(R.plurals.for_days, it, it)
        }

        bookingViewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.tvTotalPrice.text = "${PriceFormatter.format(it)} ${bookingViewModel.getCurrency()}"
        }
    }

    private fun renderFees(fees: List<Fee>) {
        binding.layoutFeesList.removeAllViews()
        fees.forEach {
            val feeBinding = ItemFeeBinding.inflate(
                layoutInflater,
                binding.layoutFeesList,
                false
            )
            feeBinding.tvTitle.text = it.name
            feeBinding.tvPrice.text = "${PriceFormatter.format(it.price)} ${bookingViewModel.getCurrency()}"
            binding.layoutFeesList.addView(feeBinding.root)
        }
    }
}