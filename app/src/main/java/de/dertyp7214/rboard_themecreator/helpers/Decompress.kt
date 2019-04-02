package de.dertyp7214.rboard_themecreator.helpers

import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class Decompress(private val zipFile: String, private val location: String) {

    init {
        dirChecker("")
    }

    fun unzip() {
        try {
            val fin = FileInputStream(zipFile)
            val zin = ZipInputStream(fin)
            var ze: ZipEntry? = null
            while ({ ze = zin.nextEntry; ze }() != null) {
                if (ze != null) {
                    if (ze!!.isDirectory) {
                        dirChecker(ze!!.name)
                    } else {
                        val fOut = FileOutputStream(location.removeSuffix("/") + "/" + ze!!.name)
                        var c = zin.read()
                        while (c != -1) {
                            fOut.write(c)
                            c = zin.read()
                        }
                        zin.closeEntry()
                        fOut.close()
                    }
                }
            }
            zin.close()
        } catch (e: Exception) {
            Log.e("Decompress", "unzip", e)
        }
    }

    private fun dirChecker(dir: String) {
        val f = File(location + dir)

        if (!f.isDirectory) f.mkdirs()
    }
}