package de.dertyp7214.rboard_themecreator.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

val File.bitmap: Bitmap?
    get() {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeFile(absolutePath, options)
    }

fun File.getFileArray(): Array<String> {
    return getFiles(this).map { it.absolutePath }.toTypedArray()
}

private fun getFiles(file: File): Array<File> {
    val list = file.listFiles().toMutableList()
    list.forEach {
        if (it.isDirectory) list.addAll(getFiles(it))
    }
    return list.filter { !it.isDirectory }.toTypedArray()
}