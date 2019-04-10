package de.dertyp7214.rboard_themecreator.helpers

import android.util.Log
import de.dertyp7214.rboard_themecreator.core.uncompressedSize
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class Decompress(private val zipFile: String, private val location: String) {

    init {
        dirChecker("")
    }

    fun unzip(progress: (progress: Long) -> Unit = {}) {
        try {
            val fin = FileInputStream(zipFile)
            val zin = ZipInputStream(fin)
            var ze: ZipEntry? = null
            val totalSize = ZipFile(zipFile).uncompressedSize()
            var currentSize = 0L
            while ({ ze = zin.nextEntry; ze }() != null) {
                if (ze != null) {
                    if (ze!!.isDirectory) {
                        dirChecker(ze!!.name)
                    } else {
                        val fOut = FileOutputStream(location.removeSuffix("/") + "/" + ze!!.name)

                        val buffer = ByteArray(8192)
                        var len = 0
                        while ({ len = zin.read(buffer); len }() != -1) {
                            fOut.write(buffer, 0, len)
                            currentSize += len
                            progress((currentSize.toFloat() / totalSize.toFloat() * 100F).toLong())
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

class Compress {
    private val buffer = 80000

    fun zip(files: Array<String>, zipFileName: String) {
        try {
            val dest = FileOutputStream(zipFileName)
            val out = ZipOutputStream(BufferedOutputStream(dest))
            val data = ByteArray(buffer)

            for (i in files.indices) {
                val fi = FileInputStream(files[i])
                val origin = BufferedInputStream(fi, buffer)

                val entry = ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1))
                out.putNextEntry(entry)
                var count: Int = -1

                while ({ count = origin.read(data, 0, buffer); count }() != -1) {
                    out.write(data, 0, count)
                }
                origin.close()
            }
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}