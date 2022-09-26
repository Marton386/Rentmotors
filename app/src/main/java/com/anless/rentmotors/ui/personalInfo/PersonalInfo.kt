package com.anless.rentmotors.ui.personalInfo

import java.util.*

data class PersonalInfo(
    val first_name: String,
    val last_name: String,
    val email: String,
    val flight_number: String,
    val comments: String,
) {
    private var citizenshipRF: Boolean? = null
    var patronymic: String? = null
    var phone: String? = null
    var country: String? = null
    var address: String? = null
    var birthday: Calendar? = null
    var passportNumber: String? = null
    var passportIssuedBy: String? = null
    var passportIssueDate: Calendar? = null
    var dlNumber: String? = null
    var dlIssueDate: Calendar? = null
    var newClient: Int? = null
    var idClient: Int? = null

    fun setClientInformation(
        patronymic: String,
        phone: String,
        country: String,
        address: String,
        birthday: Calendar?,
        passportNumber: String,
        passportIssuedBy: String,
        passportIssueDate: Calendar?,
        dlNumber: String,
        dlIssueDate: Calendar?,
        citizenshipRF: Boolean
    ) {
        this.patronymic = patronymic
        this.phone = phone
        this.country = country
        this.address = address
        this.birthday = birthday
        this.passportNumber = passportNumber
        this.passportIssuedBy = passportIssuedBy
        this.passportIssueDate = passportIssueDate
        this.dlNumber = dlNumber
        this.dlIssueDate = dlIssueDate
        this.citizenshipRF = citizenshipRF
    }

    fun setClientState(
        newClient: Int,
        idClient: Int?
    ) {
        this.newClient = newClient
        this.idClient = idClient
    }
}
