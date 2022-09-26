package com.anless.rentmotors.ui.stationsMap

import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.anless.rentmotors.models.Station
import com.anless.rentmotors.api.ResultCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import com.anless.rentmotors.repositories.StationRepository

@HiltViewModel
class StationsMapViewModel @Inject constructor(
    private val stationRepository: StationRepository
) : ViewModel() {
    val stations = MutableLiveData<List<Station>>()

    fun loadStations() {
        stationRepository.getStations (object : ResultCallback<List<Station>> {
            override fun onDataResult(data: List<Station>) {
                stations.postValue(data)
            }

            override fun onError(code: Int) {
                TODO("Not yet implemented")
            }
        })
    }
}