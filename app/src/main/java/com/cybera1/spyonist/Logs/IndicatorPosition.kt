package com.cybera1.spyonist.Logs

import android.view.Gravity

enum class IndicatorPosition(val layoutGravity: Int, val horizontal: Int) {
    TOP_LEFT(Gravity.TOP or Gravity.START, 0),
    TOP_CENTER(Gravity.TOP or Gravity.CENTER, 1),
    TOP_RIGHT(Gravity.TOP or Gravity.END , 2);

    companion object {
        fun getIndicatorPosition(vertical: Int, horizontal: Int): IndicatorPosition {
            return when {
                vertical == 0 && horizontal == 0 -> TOP_LEFT
                vertical == 0 && horizontal == 1 -> TOP_CENTER
                vertical == 0 && horizontal == 2 -> TOP_RIGHT
                else -> TOP_CENTER
            }
        }
    }
}