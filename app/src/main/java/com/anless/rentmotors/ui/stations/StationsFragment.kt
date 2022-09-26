package com.anless.rentmotors.ui.stations

import android.view.*
import android.os.Bundle
import com.anless.rentmotors.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.anless.rentmotors.databinding.FragmentStationsBinding

@AndroidEntryPoint
class StationsFragment : Fragment() {
    private val viewModel: StationsViewModel by viewModels()
    private lateinit var binding: FragmentStationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStationsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val adapter = StationsAdapter {
            val action =
                StationsFragmentDirections.actionStationsFragmentToStationInfoFragment(it.id)
            findNavController().navigate(action)
        }

        val layoutManager = LinearLayoutManager(requireContext())

        with(binding.rvStations) {
            this.layoutManager = layoutManager
            this.adapter = adapter
            this.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        }

        binding.etCity.addTextChangedListener {
            if (it.toString().trim() == "") {
                binding.imgSearch.visibility = View.VISIBLE
                binding.imgCancel.visibility = View.GONE
            } else {
                binding.imgSearch.visibility = View.GONE
                binding.imgCancel.visibility = View.VISIBLE
            }

            viewModel.setFilter(it.toString())
        }

        binding.imgCancel.setOnClickListener {
            binding.etCity.setText("")
        }

        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: StationsAdapter) {
        viewModel.filteredStations.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            with(binding.swipeRefresh) {
                isRefreshing = it
                isEnabled = it
                isRefreshing = it
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.station_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_on_map -> {
                goToMapScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToMapScreen() {
        val action = StationsFragmentDirections.actionStationsFragmentToStationsMapFragment()
        findNavController().navigate(action)
    }
}