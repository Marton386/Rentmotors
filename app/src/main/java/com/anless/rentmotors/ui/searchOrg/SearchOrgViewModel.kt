package com.anless.rentmotors.ui.searchOrg

import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.anless.rentmotors.R
import com.anless.rentmotors.utils.Result
import kotlinx.coroutines.flow.asStateFlow
import com.anless.rentmotors.models.Suggestion
import kotlinx.coroutines.flow.MutableStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import com.anless.rentmotors.repositories.OrgRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class SearchOrgViewModel @Inject constructor(
    private val orgRepository: OrgRepository
) : ViewModel() {
    private val _suggestions = MutableStateFlow<List<Suggestion>>(listOf())
    val suggestions = _suggestions.asStateFlow()
    private val _loading = MutableStateFlow(value = false)
    val loading = _loading.asStateFlow()
    private val _error = MutableStateFlow(0)
    val error = _error.asStateFlow()

    fun searchOrg(text: String) {
        if (text.length >= 3 || text.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                _loading.value = true
                val result = orgRepository.getOrgByNameOrInn(text)
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Success -> {
                            viewModelScope.launch {
                                _suggestions.emit(result.data)
                            }
                            _loading.value = false
                        }
                        is Result.Error -> {
                            _loading.value = false
                            _error.emit(R.string.failure)
                        }
                    }
                }
            }
        }
    }
}