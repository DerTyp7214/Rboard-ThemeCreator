package de.dertyp7214.rboard_themecreator.core

import android.view.View
import android.view.ViewGroup

fun View.setMargins(l: Int, t: Int, r: Int, b: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(l, t, r, b)
        requestLayout()
    }
}

fun View.setMargins(m: Int) {
    setMargins(m, m, m, m)
}