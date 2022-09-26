package com.anless.rentmotors.ui.searchCars

import android.view.View
import android.graphics.Rect
import com.anless.rentmotors.R
import android.content.Context
import androidx.recyclerview.widget.RecyclerView

class CarMarginItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val margin = context.resources.getDimensionPixelSize(R.dimen.gapMedium)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        if (position > 0) {
            outRect.top = 0
        } else {
            outRect.top = margin
        }

        outRect.left = margin
        outRect.right = margin
        outRect.bottom = margin
    }
}