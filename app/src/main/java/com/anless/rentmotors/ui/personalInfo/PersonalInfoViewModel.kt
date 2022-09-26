package com.anless.rentmotors.ui.personalInfo

import java.util.*
import javax.inject.Inject
import com.anless.rentmotors.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import com.anless.rentmotors.utils.SingleLiveEvent
import com.anless.rentmotors.helpers.SettingsHelper

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val settingsHelper: SettingsHelper
) : ViewModel() {
    private var personalInfo: PersonalInfo? = null
    val patronymic = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val country = MutableLiveData<String>()
    val citizenshipRF = MutableLiveData(true)
    val address = MutableLiveData<String>()
    val dateBirthText = MutableLiveData<String>()
    private var dateBirth: Calendar? = null
    val passportNumber = MutableLiveData<String>()
    val passportDateIssueText = MutableLiveData<String>()
    private var passportDateIssue: Calendar? = null
    val passportIssuedBy = MutableLiveData<String>()
    val dlNumber = MutableLiveData<String>()
    val dlDateIssueText = MutableLiveData<String>()
    private var dlDateIssue: Calendar? = null

    val phoneError = MutableLiveData<Int?>()
    val countryError = MutableLiveData<Int?>()
    val addressError = MutableLiveData<Int?>()
    val dateBirthError = MutableLiveData<Int?>()
    val passportNumberError = MutableLiveData<Int?>()
    val passportDateIssueError = MutableLiveData<Int?>()
    val passportIssuedByError = MutableLiveData<Int?>()

    val dlNumberError = MutableLiveData<Int?>()
    val dlDateIssueError = MutableLiveData<Int?>()

    val personalInfoReadyEvent = SingleLiveEvent<PersonalInfo>()
    val showErrorEvent = SingleLiveEvent<Int>()

    init {
        dateBirthText.observeForever {
            dateBirth = getDateCalendar(it)
        }

        passportDateIssueText.observeForever{
            passportDateIssue = getDateCalendar(it)
        }

        dlDateIssueText.observeForever{
            dlDateIssue = getDateCalendar(it)
        }
    }

    fun setPersonalInfo(info: PersonalInfo?){
        personalInfo = info
    }

    private fun getDateCalendar(stringDate: String): Calendar? {
        val date = Calendar.getInstance()
        return try {
            val dateSplit = stringDate.split('/')
            date.set(Calendar.DAY_OF_MONTH, dateSplit[0].toInt())
            date.set(Calendar.MONTH, dateSplit[1].toInt())
            date.set(Calendar.YEAR, dateSplit[2].toInt())
            date
        } catch (e: Exception) {
            null
        }
    }

    fun prepareData() {
        if (!validateForm()) {
            showErrorEvent.postValue(R.string.not_all_fields_filled)
        } else {
            if (personalInfo != null) {
                personalInfo?.let {
                    personalInfo!!.setClientInformation(
                        patronymic.value ?: "",
                        phone.value ?: "",
                        country.value ?: "",
                        address.value ?: "",
                        dateBirth,
                        passportNumber.value ?: "",
                        passportIssuedBy.value ?: "",
                        passportDateIssue,
                        dlNumber.value ?: "",
                        dlDateIssue,
                        citizenshipRF.value ?: true
                    )
                    personalInfoReadyEvent.postValue(personalInfo!!)
                }

            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true
        hideErrors()

        if (!validatePhone()) {
            isValid = false
        }

        if (!validateCountry()) {
            isValid = false
        }

        if (!validateAddress()) {
            isValid = false
        }

        if (!validateDateBirth()) {
            isValid = false
        }

        if (!validatePassportNumber()) {
            isValid = false
        }

        if (!validatePassportIssuedBy()) {
            isValid = false
        }

        if (!validatePassportDateIssue()) {
            isValid = false
        }

        if (!validateDLNumber()) {
            isValid = false
        }

        if (!validateDlDateIssue()) {
            isValid = false
        }

        return isValid
    }

    private fun hideErrors() {
        phoneError.postValue(null)
        addressError.postValue(null)
        dateBirthError.postValue(null)
        passportNumberError.postValue(null)
        passportDateIssueError.postValue(null)
        countryError.postValue(null)
        passportIssuedByError.postValue(null)
        dlNumberError.postValue(null)
    }

    private fun validatePhone(): Boolean {
        val phoneValue = phone.value

        if (phoneValue.isNullOrEmpty()) {
            phoneError.postValue(R.string.required_field)
        } else if (phoneValue.length < 10) {
            phoneError.postValue(R.string.wrong_phone_format)
            return false
        }

        return true
    }

    private fun validateCountry(): Boolean {
        if (country.value.isNullOrEmpty()) {
            countryError.postValue(R.string.required_field)
            return false
        }
        return true
    }

    private fun validateAddress(): Boolean {
        if (address.value.isNullOrEmpty()) {
            addressError.postValue(R.string.required_field)
            return false
        }

        return true
    }

    private fun validateDateBirth(): Boolean {
        if (dateBirth == null) {
            dateBirthError.postValue(R.string.wrong_date_format)
            return false
        }
        return true
    }

    private fun validatePassportNumber(): Boolean {
        if (passportNumber.value.isNullOrEmpty()) {
            passportNumberError.postValue(R.string.required_field)
            return false
        }

        return true
    }

    private fun validatePassportDateIssue(): Boolean {
        if (passportDateIssue == null) {
            passportDateIssueError.postValue(R.string.wrong_date_format)
            return false
        }
        return true
    }

    private fun validatePassportIssuedBy(): Boolean {
        if (passportIssuedBy.value.isNullOrEmpty()) {
            passportIssuedByError.postValue(R.string.required_field)
            return false
        }

        return true
    }

    private fun validateDLNumber(): Boolean {
        if (dlNumber.value.isNullOrEmpty()) {
            dlNumberError.postValue(R.string.required_field)
            return false
        }

        return true
    }

    private fun validateDlDateIssue(): Boolean {
        if (dlDateIssue == null) {
            dlDateIssueError.postValue(R.string.wrong_date_format)
            return false
        }
        return true
    }

    fun setCountry(city: String) {
        this.country.postValue(city)
    }
}