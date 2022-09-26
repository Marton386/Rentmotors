package com.anless.rentmotors.ui.stations

import javax.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.anless.rentmotors.models.Station
import com.anless.rentmotors.api.ResultCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import com.anless.rentmotors.repositories.StationRepository

@HiltViewModel
class StationsViewModel @Inject constructor(
    private val stationRepository: StationRepository
) : ViewModel() {
    private val stations = arrayListOf<Station>()
    private val filter = MutableLiveData("")
    val filteredStations: LiveData<List<Station>> = Transformations.map(filter) {
        getFilteredStations(it)
    }

    val isLoading = MutableLiveData<Boolean>()

    init {
        loadStations()
    }

    private fun loadStations() {
        isLoading.postValue(true)
        stationRepository.getStations (object : ResultCallback<List<Station>> {
            override fun onDataResult(data: List<Station>) {
                stations.clear()
                stations.addAll(data)
                isLoading.postValue(false)
            }

            override fun onError(code: Int) {
                TODO("Not yet implemented")
            }
        })
    }

    fun setFilter(filter: String) {
        this.filter.value = filter
    }

    private fun getFilteredStations(filter: String): List<Station> {
        return if (filter.isBlank()) {
            stations
        } else {
            stations.filter {
                it.name.startsWith(filter, ignoreCase = true)
                        || it.address.contains(filter, ignoreCase = true)
                        || it.shortCode.contains(filter, ignoreCase = true)
                        || it.name.contains(filter, ignoreCase = true)
            }
        }
    }
}