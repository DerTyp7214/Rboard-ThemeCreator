package de.dertyp7214.rboard_themecreator.core

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun InputStream.saveAs(path: String): File {
    val buffer = ByteArray(available())
    read(buffer)
    val file = File(path)
    val out = FileOutputStream(file)
    out.write(buffer)
    return file
}