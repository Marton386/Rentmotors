package com.anless.rentmotors.ui.checkClient

import android.net.Uri
import android.view.View
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.view.ViewGroup
import com.anless.rentmotors.R
import android.app.AlertDialog
import android.widget.TextView
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anless.rentmotors.ui.startBooking.BookingViewModel
import com.anless.rentmotors.databinding.FragmentCheckClientBinding
import com.anless.rentmotors.models.StationType
import com.anless.rentmotors.ui.startBooking.StartBookingFragmentDirections
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CheckClientFragment : Fragment() {
    private val bookingViewModel: BookingViewModel by activityViewModels()
    private val viewModel: CheckClientViewModel by viewModels()
    private lateinit var binding: FragmentCheckClientBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckClientBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnNext.setOnClickListener {
            if ((viewModel.orgCheck.value == true && binding.tvOrganization.text != "") ||
                viewModel.orgCheck.value != true) {
                viewModel.prepareData()
            } else {
                if (viewModel.orgCheck.value == true) {
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                        .setView(R.layout.dialog_message)
                    val dialog = alertDialogBuilder.show()
                    dialog.findViewById<TextView>(R.id.tvMessage).text = requireContext()
                        .getString(R.string.choose_org)
                    dialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
                        dialog.dismiss()
                    }
                }
            }
        }

        binding.tvRentTerms.setOnClickListener {
            openLink(getString(R.string.rent_terms_link))
        }

        binding.tvPrivacyPolicy.setOnClickListener {
            openLink(getString(R.string.privacy_policy_link))
        }

        binding.layoutOrg.setOnClickListener {
            toSearchOrgFragment()
        }

        subscribeUi()
    }

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            bookingViewModel.organization.collectLatest {
                if (it == null) {
                    binding.tvOrganization.text = ""
                } else {
                    binding.tvOrganization.text = it.value
                }
            }
        }

        viewModel.orgCheck.observe(viewLifecycleOwner) {
            if (it) {
                binding.layoutOrg.visibility = View.VISIBLE
            } else {
                binding.layoutOrg.visibility = View.GONE
            }
            bookingViewModel.setOrgCheck(it)
        }

        viewModel.showErrorEvent.observe(viewLifecycleOwner) {
            showError(it)
        }

        viewModel.personalInfoReadyEvent.observe(viewLifecycleOwner) {
            bookingViewModel.sendData(it)
        }

        viewModel.isNewClientEvent.observe(viewLifecycleOwner) {
            bookingViewModel.setPersonalInfo(it)
            val action = CheckClientFragmentDirections
                .actionCheckClientFragmentToPersonalInfoFragment()
            findNavController().navigate(action)
        }

        bookingViewModel.bookHasCreatedEvent.observe(viewLifecycleOwner) {
            val action = CheckClientFragmentDirections
                .actionCheckClientFragmentToFinishBookingFragment()
            findNavController().navigate(action)
        }

        bookingViewModel.error.observe(viewLifecycleOwner) {
            showError(it)
        }

        viewModel.acceptTerms.observe(viewLifecycleOwner) {
            binding.btnNext.isEnabled = it
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.INVISIBLE
            if (binding.cbAcceptTerms.isChecked) {
                binding.btnNext.isEnabled = !it
            }
        }
    }

    private fun toSearchOrgFragment() {
        val action = CheckClientFragmentDirections.actionCheckClientFragmentToSearchOrgFragment()
        findNavController().navigate(action)
    }

    private fun openLink(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(browserIntent)
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