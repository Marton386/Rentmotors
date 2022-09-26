package com.anless.rentmotors.ui.searchCars

import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.anless.rentmotors.api.ErrorCodes
import com.anless.rentmotors.api.ResultCallback
import com.anless.rentmotors.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import com.anless.rentmotors.models.SearchCarsWrapper
import com.anless.rentmotors.repositories.CarRepository

@HiltViewModel
class SearchCarsViewModel @Inject constructor(
    private val carsRepository: CarRepository
) : ViewModel() {
    private var searchCarFilter: SearchCarFilter? = null
    val isLoading = MutableLiveData<Boolean>()
    val searchCarsWrapper = MutableLiveData<SearchCarsWrapper>()
    val error = SingleLiveEvent<Int>()

    fun setCarFilter(searchCarFilter: SearchCarFilter) {
        this.searchCarFilter = searchCarFilter
        loadCars()
    }

    fun loadCars() {
        searchCarFilter?.let { carFilter ->
            isLoading.postValue(true)
            carsRepository.getCars(carFilter, object : ResultCallback<SearchCarsWrapper> {
                override fun onDataResult(data: SearchCarsWrapper) {
                    searchCarsWrapper.postValue(data)
                    isLoading.postValue(false)
                }

                override fun onError(code: Int) {
                    error.postValue(ErrorCodes.getMessageByCode(code))
                    isLoading.postValue(false)
                }
            })
        }
    }
}