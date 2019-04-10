package de.dertyp7214.rboard_themecreator.core

import android.widget.TextView
import androidx.appcompat.widget.Toolbar

fun Toolbar.getTextViewTitle(): TextView? {
    var textViewTitle: TextView? = null
    for (i in 0 until childCount) {
        val view = getChildAt(i)
        if (view is TextView) {
            textViewTitle = view
            break
        }
    }
    return textViewTitle
}