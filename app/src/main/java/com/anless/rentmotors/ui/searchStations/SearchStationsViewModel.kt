package com.anless.rentmotors.ui.searchStations

import javax.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.anless.rentmotors.api.ErrorCodes
import com.anless.rentmotors.models.Station
import com.anless.rentmotors.api.ResultCallback
import com.anless.rentmotors.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import com.anless.rentmotors.repositories.StationRepository

@HiltViewModel
class SearchStationsViewModel @Inject constructor(
    private val stationRepository: StationRepository
) : ViewModel() {

    private val stations = MutableLiveData<List<Station>>()
    private val filter = MutableLiveData("")
    val filteredStations: LiveData<List<Station>> = Transformations.map(filter) {
        getFilteredStations(it)
    }
    val error = SingleLiveEvent<Int>()

    init {
        loadStations()
    }

    private fun loadStations() {
        stationRepository.getStations(object : ResultCallback<List<Station>> {
            override fun onDataResult(data: List<Station>) {
                stations.postValue(data)
            }

            override fun onError(code: Int) {
                error.postValue(ErrorCodes.getMessageByCode(code))
            }
        })
    }

    fun setFilter(filter: String) {
        this.filter.value = filter
    }

    private fun getFilteredStations(filter: String): List<Station> {
        return if (filter.isBlank()) {
            listOf()
        } else {
            val stationList = stations.value ?: listOf()
            stationList.filter {
                it.name.startsWith(filter, ignoreCase = true)
                        || it.name.contains(filter, ignoreCase = true)
            }
        }
    }
}