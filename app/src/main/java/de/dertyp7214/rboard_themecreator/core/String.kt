package de.dertyp7214.rboard_themecreator.core

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.util.Log
import java.io.DataOutputStream
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

fun String.runAsCommand(): Boolean {
    return try {
        Log.d("COMMAND", this)
        val process = Runtime.getRuntime().exec("su")
        val os = DataOutputStream(process.outputStream)

        os.writeBytes(this + "\n")

        os.writeBytes("exit\n")
        os.flush()
        os.close()

        process.waitFor()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun String.appInstalled(pm: PackageManager): Boolean {
    try {
        pm.getPackageInfo(this, PackageManager.GET_ACTIVITIES)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
    }
    return false
}

fun String.open(context: Context): () -> Unit {
    return { context.startActivity(context.packageManager.getLaunchIntentForPackage(this)) }
}