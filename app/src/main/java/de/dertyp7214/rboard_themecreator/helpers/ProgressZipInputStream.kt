package de.dertyp7214.rboard_themecreator.helpers

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.io.InputStream
import java.util.zip.ZipInputStream

class ProgressZipInputStream(`in`: InputStream, val maxNumBytes: Long) : ZipInputStream(`in`) {
    private val propertyChangeSupport: PropertyChangeSupport = PropertyChangeSupport(this)
    @Volatile
    var totalNumBytesRead: Long = 0
        private set

    fun addPropertyChangeListener(l: PropertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(l)
    }

    fun removePropertyChangeListener(l: PropertyChangeListener) {
        propertyChangeSupport.removePropertyChangeListener(l)
    }

    override fun read(): Int {
        val b = super.read()
        updateProgress(1)
        return b
    }

    override fun read(b: ByteArray): Int {
        return updateProgress(super.read(b).toLong()).toInt()
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        return updateProgress(super.read(b, off, len).toLong()).toInt()
    }

    override fun skip(n: Long): Long {
        return updateProgress(super.skip(n))
    }

    override fun mark(readlimit: Int) {
        throw UnsupportedOperationException()
    }

    override fun reset() {
        throw UnsupportedOperationException()
    }

    override fun markSupported(): Boolean {
        return false
    }

    private fun updateProgress(numBytesRead: Long): Long {
        if (numBytesRead > 0) {
            val oldTotalNumBytesRead = this.totalNumBytesRead
            this.totalNumBytesRead += numBytesRead
            propertyChangeSupport.firePropertyChange("totalNumBytesRead", oldTotalNumBytesRead, this.totalNumBytesRead)
        }

        return numBytesRead
    }
}