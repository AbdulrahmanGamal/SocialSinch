package com.vanniktech.emoji

import android.content.Context
import android.graphics.Point
import android.util.TypedValue
import android.view.View
import android.widget.PopupWindow

private val DONT_UPDATE_FLAG = -1

fun <T> checkNotNull(reference: T?, message: String): T =
    reference ?: throw IllegalArgumentException(message)

fun Context.dpToPx(dp: Float): Int =
    (dp * resources.displayMetrics.density).toInt()

fun View.locationOnScreen(): Point {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return Point(location[0], location[1])
}

fun PopupWindow.fixPopupLocation(desiredLocation: Point) {
    contentView.post {
        val actualLocation = contentView.locationOnScreen()

        if (!(actualLocation.x == desiredLocation.x && actualLocation.y == desiredLocation.y)) {
            val differenceX = actualLocation.x - desiredLocation.x
            val differenceY = actualLocation.y - desiredLocation.y

            val fixedOffsetX = when (actualLocation.x > desiredLocation.x) {
               true -> desiredLocation.x - differenceX
               else -> desiredLocation.x + differenceX
            }

            val fixedOffsetY = when (actualLocation.y > desiredLocation.y) {
                true -> desiredLocation.y - differenceY
                else -> desiredLocation.y + differenceY
            }
            update(fixedOffsetX, fixedOffsetY, DONT_UPDATE_FLAG, DONT_UPDATE_FLAG)
        }
    }
}

fun Context.getToolbarHeight(): Int {
    val tv = TypedValue()
    if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        return TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    }
    return 0
}