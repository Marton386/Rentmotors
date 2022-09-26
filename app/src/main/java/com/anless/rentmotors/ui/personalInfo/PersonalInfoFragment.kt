package com.anless.rentmotors.ui.personalInfo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.text.InputType
import android.view.ViewGroup
import com.anless.rentmotors.R
import android.app.AlertDialog
import android.widget.TextView
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.anless.rentmotors.ui.startBooking.BookingViewModel
import com.anless.rentmotors.databinding.FragmentPersonalInfoBinding

@AndroidEntryPoint
class PersonalInfoFragment : Fragment() {
    private val bookingViewModel: BookingViewModel by activityViewModels()
    private val viewModel: PersonalInfoViewModel by viewModels()
    private lateinit var binding: FragmentPersonalInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonalInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.setPersonalInfo(bookingViewModel.getPersonalInfo())

        binding.btnCompleteBooking.setOnClickListener {
            viewModel.prepareData()
        }

        binding.etDateOfBirth.listen()
        binding.etPassportDateIssue.listen()
        binding.etDLDateIssue.listen()

        viewModel.setCountry(getString(R.string.russia))
        binding.swCitizenshipRF.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setCountry(getString(R.string.russia))
                binding.layoutPassportIssuedByCountry.visibility = View.GONE
                binding.tvPassportIssuedByCountry.visibility = View.GONE
                binding.tvPassportIssuedBy.visibility = View.VISIBLE
                binding.layoutPassportIssuedBy.visibility = View.VISIBLE
                binding.tvRegistrationAddress.visibility = View.VISIBLE
                binding.layoutRegistrationAddress.visibility = View.VISIBLE
                binding.tvPassportSeriesNumber.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
                binding.tvDLSeriesNumber.inputType = InputType.TYPE_CLASS_NUMBER
            } else {
                viewModel.setCountry("")
                binding.layoutPassportIssuedByCountry.visibility = View.VISIBLE
                binding.tvPassportIssuedByCountry.visibility = View.VISIBLE
                binding.tvPassportIssuedBy.visibility = View.GONE
                binding.layoutPassportIssuedBy.visibility = View.GONE
                binding.tvRegistrationAddress.visibility = View.GONE
                binding.layoutRegistrationAddress.visibility = View.GONE
                binding.tvPassportSeriesNumber.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                binding.tvDLSeriesNumber.inputType = InputType.TYPE_CLASS_TEXT
            }
        }
        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.showErrorEvent.observe(viewLifecycleOwner) {
            showError(it)
        }

        viewModel.personalInfoReadyEvent.observe(viewLifecycleOwner) {
            bookingViewModel.sendData(it)
        }

        bookingViewModel.bookHasCreatedEvent.observe(viewLifecycleOwner) {
            val action =
                PersonalInfoFragmentDirections.actionPersonalInfoFragmentToFinishBookingFragment()
            findNavController().navigate(action)
        }

        bookingViewModel.error.observe(viewLifecycleOwner) {
            showError(it)
        }

        bookingViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.INVISIBLE
            binding.btnCompleteBooking.isEnabled = !it
        }

        viewModel.citizenshipRF.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.setCountry(getString(R.string.russia))
                binding.layoutPassportIssuedByCountry.visibility = View.GONE
                binding.tvPassportIssuedByCountry.visibility = View.GONE
                binding.tvPassportIssuedBy.visibility = View.VISIBLE
                binding.layoutPassportIssuedBy.visibility = View.VISIBLE
                binding.tvRegistrationAddress.visibility = View.VISIBLE
                binding.layoutRegistrationAddress.visibility = View.VISIBLE
                binding.etPassportSeriesNumber.inputType = InputType.TYPE_CLASS_NUMBER
                binding.etDLSeriesNumber.inputType = InputType.TYPE_CLASS_NUMBER
            } else {
                viewModel.setCountry("")
                binding.layoutPassportIssuedByCountry.visibility = View.VISIBLE
                binding.tvPassportIssuedByCountry.visibility = View.VISIBLE
                binding.tvPassportIssuedBy.visibility = View.GONE
                binding.layoutPassportIssuedBy.visibility = View.GONE
                binding.tvRegistrationAddress.visibility = View.GONE
                binding.layoutRegistrationAddress.visibility = View.GONE
                binding.etPassportSeriesNumber.inputType = InputType.TYPE_CLASS_TEXT
                binding.etDLSeriesNumber.inputType = InputType.TYPE_CLASS_TEXT
            }
        }
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
}