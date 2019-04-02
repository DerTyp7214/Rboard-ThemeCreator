package de.dertyp7214.rboard_themecreator.core

import android.content.Context

fun Float.dp(context: Context): Float {
    val scale = context.resources.displayMetrics.density
    return this * scale + 0.5f
}