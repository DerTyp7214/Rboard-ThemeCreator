package de.dertyp7214.rboard_themecreator.core

import android.content.Context
import android.content.Intent

fun <T> Class<T>.getIntent(context: Context): Intent {
    return Intent(context, this)
}