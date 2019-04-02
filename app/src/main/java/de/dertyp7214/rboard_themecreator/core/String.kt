package de.dertyp7214.rboard_themecreator.core

import android.graphics.Color

fun String.isHexColor(): Boolean {
    return try {
        Color.parseColor(this)
        true
    } catch (ex: Exception) {
        false
    }
}