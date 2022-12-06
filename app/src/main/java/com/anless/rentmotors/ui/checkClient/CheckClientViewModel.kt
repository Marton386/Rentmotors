package com.anless.rentmotors.ui.checkClient

import javax.inject.Inject
import android.util.Patterns
import com.anless.rentmotors.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.anless.rentmotors.api.ErrorCodes
import com.anless.rentmotors.api.ResultCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import com.anless.rentmotors.utils.SingleLiveEvent
import com.anless.rentmotors.api.entities.ClientInfoDTO
import com.anless.rentmotors.ui.personalInfo.PersonalInfo
import com.anless.rentmotors.repositories.CheckClientRepository

@HiltViewModel
class CheckClientViewModel @Inject constructor(
    private val checkClientRepository: CheckClientRepository
): ViewModel() {
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val numFlight = MutableLiveData<String>()
    val comment = MutableLiveData<String>()

    val firstNameError = MutableLiveData<Int?>()
    val lastNameError = MutableLiveData<Int?>()
    val emailError = MutableLiveData<Int?>()

    val acceptTerms = MutableLiveData(false)
    val personalInfoReadyEvent = SingleLiveEvent<PersonalInfo>()
    val showErrorEvent = SingleLiveEvent<Int>()

    val orgCheck = MutableLiveData(false)

    val isNewClientEvent = SingleLiveEvent<PersonalInfo>()
    val isLoading = MutableLiveData(false)
    val error = SingleLiveEvent<Int>()

    fun prepareData() {
        if (!validateForm()) {
            showErrorEvent.postValue(R.string.not_all_fields_filled)
        } else {
            val personalInfo = PersonalInfo(
                firstName.value ?: "",
                lastName.value ?: "",
                email.value ?: "",
                numFlight.value ?: "",
                comment.value ?: ""
            )
            checkClient(personalInfo)
        }
    }

    fun checkClient(personalInfo: PersonalInfo){
        isLoading.postValue(true)
        checkClientRepository.checkClient(
            personalInfo.last_name,
            personalInfo.email,
            object : ResultCallback<ClientInfoDTO>{
                override fun onDataResult(data: ClientInfoDTO) {
                    personalInfo.setClientState(data.newClient, data.idClient)
                    when(data.idClient) {
                        null -> {
                            isLoading.postValue(false)
                            isNewClientEvent.postValue(personalInfo)
                        }
                        else -> personalInfoReadyEvent.postValue(personalInfo)
                    }
                }
                override fun onError(code: Int) {
                    error.postValue(ErrorCodes.getMessageByCode(code))
                    isLoading.postValue(false)
                }
            }
        )
    }

    private fun validateForm(): Boolean {
        var isValid = true
        hideErrors()
        if (!validateFirstName()) {
            isValid = false
        }
        if (!validateLastName()) {
            isValid = false
        }
        if (!validateEmail()) {
            isValid = false
        }
        return isValid
    }

    private fun hideErrors() {
        firstNameError.postValue(null)
        lastNameError.postValue(null)
        emailError.postValue(null)
    }

    private fun validateFirstName(): Boolean {
        if (firstName.value.isNullOrEmpty()) {
            firstNameError.postValue(R.string.required_field)
            return false
        }
        return true
    }

    private fun validateLastName(): Boolean {
        if (lastName.value.isNullOrEmpty()) {
            lastNameError.postValue(R.string.required_field)
            return false
        }
        return true
    }

    private fun validateEmail(): Boolean {
        val emailValue = email.value
        if (emailValue.isNullOrEmpty()) {
            emailError.postValue(R.string.required_field)
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            emailError.postValue(R.string.wrong_email_format)
            return false
        }
        return true
    }
}