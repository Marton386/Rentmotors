package com.anless.rentmotors.ui.finishBooking

import java.io.File
import android.net.Uri
import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.content.Intent
import android.view.ViewGroup
import com.anless.rentmotors.R
import android.app.AlertDialog
import android.widget.TextView
import com.karumi.dexter.Dexter
import android.view.LayoutInflater
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.anless.rentmotors.BuildConfig
import com.karumi.dexter.PermissionToken
import androidx.core.content.FileProvider
import com.anless.rentmotors.ui.MainActivity
import com.anless.rentmotors.models.BookStatus
import androidx.fragment.app.activityViewModels
import com.anless.rentmotors.utils.DateFormatter
import com.karumi.dexter.listener.PermissionRequest
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.anless.rentmotors.ui.startBooking.BookingViewModel
import com.anless.rentmotors.databinding.FragmentFinishBookingBinding

class FinishBookingFragment : Fragment() {
    private val bookingViewModel: BookingViewModel by activityViewModels()
    private lateinit var binding: FragmentFinishBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().popBackStack(R.id.startBookingFragment, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishBookingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFinish.setOnClickListener {
            findNavController().popBackStack(R.id.startBookingFragment, false)
        }

        binding.layoutAddToWallet.setOnClickListener {
            loadVoucher()
        }
        subscribeUi()
    }

    private fun subscribeUi() {
        bookingViewModel.dateFrom.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvPickUpDate.text = DateFormatter.formatRentDateTime(it)
            }
        }

        bookingViewModel.stationPickUp.observe(viewLifecycleOwner) {
            binding.tvPickUpStation.text = it.address
        }

        bookingViewModel.book.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvNumberReservation.text = it.res_id

                if (it.status == BookStatus.CONFIRM) {
                    binding.tvBookingState.text = getString(R.string.booking_finished)
                    binding.tvTitlePickUpInfo.text = getString(R.string.we_are_waiting_you)
                    binding.tvVoucherInfo.visibility = View.VISIBLE
                    binding.layoutAddToWallet.visibility = View.VISIBLE
                } else {
                    binding.tvBookingState.text = getString(R.string.booking_almost_finished)
                    binding.tvTitlePickUpInfo.text = getString(R.string.pick_up_car)
                    binding.tvVoucherInfo.visibility = View.GONE
                    binding.layoutAddToWallet.visibility = View.GONE
                }
            }
        }

        bookingViewModel.voucher.observe(viewLifecycleOwner) {
            openVoucher(it)
        }

        bookingViewModel.error.observe(viewLifecycleOwner) {
            showError(it)
        }

        bookingViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.INVISIBLE
            binding.btnFinish.isEnabled = !it
            binding.layoutAddToWallet.isEnabled = !it
        }
    }

    private fun openVoucher(file: File) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri: Uri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".fileprovider",
                file
            )
            intent.data = uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val chooserIntent = Intent.createChooser(intent, getString(R.string.open_file_with))
            startActivity(chooserIntent)
        } catch (e: Exception) {
            showError(R.string.could_not_open_file)
            e.printStackTrace()
        }
    }

    private fun showError(error: Int) {
        showDialog(R.string.error_label, error)
    }

    private fun showDialog(title: Int, message: Int, listener: (() -> Unit)? = null) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_message)
        val dialog = alertDialogBuilder.show()
        dialog.findViewById<TextView>(R.id.tvTitle).setText(title)
        dialog.findViewById<TextView>(R.id.tvMessage).setText(message)
        dialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
            listener?.invoke()
            dialog.dismiss()
        }
    }

    private fun loadVoucher() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    bookingViewModel.loadVoucher()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    showError(R.string.permission_not_granted)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    showDialog(R.string.required_permission, R.string.to_write_external_storage_rationale) {
                        p1?.continuePermissionRequest()
                    }
                }
            })
            .check()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setToolbarVisible(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).setToolbarVisible(true)
    }
}