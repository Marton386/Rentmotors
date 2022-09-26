package com.anless.rentmotors.models

import com.anless.rentmotors.utils.PriceFormatter

data class Extra(
    val code: String,
    val name: String,
    val description: String,
    val price: Double,
    val currency: String,
    val amount: Int,
    val maxCount: Int,
    val address: String,
    val perDayPay: Boolean
) {
    fun getShowPrice(): String {
        val showPrice = if (amount > 0) price * amount else price
        return PriceFormatter.format(showPrice)
    }

    fun getTotalPrice() = amount * price

    fun isSelected() = amount > 0

    fun isMultiSelect() = maxCount > 1

    fun isChildSeat(): Boolean {
        return code == ExtraType.CHILD_SEAT0
                || code == ExtraType.CHILD_SEAT1
                || code == ExtraType.CHILD_SEAT2
                || code == ExtraType.CHILD_SEAT3
    }

    fun isInputVisible(): Boolean {
        return (code == ExtraType.DELIVERY || code == ExtraType.COLLECT)
                && isSelected()
    }
}