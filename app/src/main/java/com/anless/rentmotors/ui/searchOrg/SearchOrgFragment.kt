package com.anless.rentmotors.ui.searchOrg

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.anless.rentmotors.R
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import com.anless.rentmotors.ui.MainActivity
import androidx.fragment.app.activityViewModels
import com.anless.rentmotors.utils.AndroidUtils
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anless.rentmotors.ui.startBooking.BookingViewModel
import com.anless.rentmotors.databinding.FragmentSearchOrgBinding
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchOrgFragment : Fragment() {
    private val bookingViewModel: BookingViewModel by activityViewModels()
    private val viewModel: SearchOrgViewModel by viewModels()
    private lateinit var binding: FragmentSearchOrgBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchOrgBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AndroidUtils.openKeyboard(binding.etOrg)
        (activity as MainActivity).setTitle(getString(R.string.choose_org))

        val adapter = SearchOrgAdapter {
            bookingViewModel.setOrganization(it)
            findNavController().popBackStack()
        }
        val layoutManager = LinearLayoutManager(requireContext())
        with(binding.rvSearchOrg) {
            this.layoutManager = layoutManager
            this.adapter = adapter
        }

        binding.etOrg.addTextChangedListener {
            if (it.toString().trim() == "") {
                binding.imgSearch.visibility = View.VISIBLE
                binding.imgCancel.visibility = View.GONE
            } else {
                binding.imgSearch.visibility = View.GONE
                binding.imgCancel.visibility = View.VISIBLE
            }
            val partOrg = it.toString().trim()
            viewModel.searchOrg(partOrg)
        }
        binding.imgCancel.setOnClickListener {
            binding.etOrg.setText("")
        }
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: SearchOrgAdapter) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.suggestions.collectLatest {
                adapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loading.collectLatest {
                if (it) {
                    adapter.submitList(listOf())
                    binding.rvSearchOrg.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                else {
                    binding.rvSearchOrg.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest { resError ->
                if (resError != 0) {
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                        .setView(R.layout.dialog_message)
                    val dialog = alertDialogBuilder.show()
                    dialog.findViewById<TextView>(R.id.tvMessage).text = requireContext()
                        .getString(resError)
                    dialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
                        dialog.dismiss()
                    }
                }
            }
        }
    }
}