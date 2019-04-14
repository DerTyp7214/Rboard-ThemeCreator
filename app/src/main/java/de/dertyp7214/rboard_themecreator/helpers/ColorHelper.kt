package de.dertyp7214.rboard_themecreator.helpers

import android.content.Context
import android.graphics.Color

object ColorHelper {
    fun getAttrColor(context: Context, style: Int): Int {
        val typedArrayDark = context.obtainStyledAttributes(
            intArrayOf(style)
        )
        val color = typedArrayDark.getColor(0, Color.DKGRAY)
        typedArrayDark.recycle()
        return color
    }
}