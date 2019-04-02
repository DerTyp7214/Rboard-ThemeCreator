package de.dertyp7214.rboard_themecreator.core

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

fun Context.nextActivity(): AppCompatActivity? {
    return when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.nextActivity()
        else -> null
    }
}

fun Context.readFileAsString(file: String): String {
    return BufferedReader(InputStreamReader(FileInputStream(File(file)))).use { it.readText() }
}