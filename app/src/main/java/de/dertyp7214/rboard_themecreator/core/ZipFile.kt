package de.dertyp7214.rboard_themecreator.core

import java.util.zip.ZipFile

fun ZipFile.uncompressedSize(): Long {
    val e = entries()
    var totalSize = 0L
    while (e.hasMoreElements()) {
        val ze = e.nextElement()
        totalSize += ze.size
    }
    return totalSize
}