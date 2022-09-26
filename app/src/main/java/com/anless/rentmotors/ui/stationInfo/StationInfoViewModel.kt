package com.anless.rentmotors.ui.stationInfo

import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.anless.rentmotors.models.Station
import com.anless.rentmotors.api.ResultCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import com.anless.rentmotors.repositories.StationRepository

@HiltViewModel
class StationInfoViewModel @Inject constructor(
    private val stationRepository: StationRepository
) : ViewModel() {
    val station = MutableLiveData<Station>()

    fun getStation(id: Int) {
        stationRepository.getStation(id, object : ResultCallback<Station?> {
            override fun onDataResult(data: Station?) {
                if (data != null) {
                    station.postValue(data)
                }
            }

            override fun onError(code: Int) {
                TODO("Not yet implemented")
            }
        })
    }
}