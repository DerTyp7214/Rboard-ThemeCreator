package de.dertyp7214.rboard_themecreator.core

import android.graphics.Color
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

fun String.isHexColor(): Boolean {
    return try {
        Color.parseColor(this)
        true
    } catch (ex: Exception) {
        false
    }
}

fun String.writeToFile(file: File) {
    try {
        val outputStreamWriter = OutputStreamWriter(FileOutputStream(file))
        outputStreamWriter.write(this)
        outputStreamWriter.close()
    } catch (e: Exception) {
        Log.e("Exception", "File write failed: $e")
    }
}

fun String.toggleCssAndroidAlpha(toCss: Boolean = false): String {
    return if (this.removePrefix("#").length == 8) "#${this.removePrefix("#").substring(
        if (toCss) 2 else 6,
        8
    )}${this.removePrefix("#").substring(0, if (toCss) 2 else 6)}" else "#${this.removePrefix("#")}"
}