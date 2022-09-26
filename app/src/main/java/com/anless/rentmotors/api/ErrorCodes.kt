package com.anless.rentmotors.api

import com.anless.rentmotors.R

object ErrorCodes {
    const val FAILURE = -1
    const val REQUEST_FAILURE = -2
    private const val ALREADY_BOOKED = 114

    fun getMessageByCode(code: Int): Int {
        return when(code) {
            FAILURE -> R.string.failure
            REQUEST_FAILURE -> R.string.failure_get_data
            ALREADY_BOOKED -> R.string.already_booked
            else -> R.string.error_label
        }
    }
}