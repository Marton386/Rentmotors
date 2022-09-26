package com.anless.rentmotors.utils

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("isGone")
fun visibleGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) View.GONE else View.VISIBLE
}

@BindingAdapter("isVisible")
fun visibleInvisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("error")
fun error(inputLayout: TextInputLayout, errorRes: Int?) {
    if (errorRes == null) {
        inputLayout.error = null
    } else {
        inputLayout.error = inputLayout.context.getString(errorRes)
    }
}