package de.dertyp7214.rboard_themecreator.core

import android.content.Context

fun Double.dp(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}