package de.dertyp7214.rboard_themecreator.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.adapter.ThemeAdapter
import kotlinx.android.synthetic.main.activity_theme_overview.*
import java.io.File
import java.util.*

class ThemeOverview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_overview)

        val gboardTheme = File("/etc/gboard_theme")

        if (gboardTheme.isDirectory) {
            val files = gboardTheme.listFiles()
            val zips = files.filter {
                it.isFile && it.name.endsWith(".zip")
            }
            val themes = ArrayList<GboardTheme>()
            zips.forEach {
                val image = files.find { img -> img.name == it.name.removeSuffix(".zip") }
                themes.add(GboardTheme(it, image))
            }
            ThemeAdapter(this, rv, themes)
        }
    }
}

data class GboardTheme(val zipFile: File, val image: File?) {
    override fun toString(): String {
        return "${javaClass.name} zipFile: ${zipFile.absolutePath} image: ${image?.absolutePath ?: "null"}"
    }
}