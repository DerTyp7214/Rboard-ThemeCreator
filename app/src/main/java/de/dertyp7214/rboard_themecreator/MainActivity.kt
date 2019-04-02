package de.dertyp7214.rboard_themecreator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.dertyp7214.rboard_themecreator.adapter.CssDefAdapter
import de.dertyp7214.rboard_themecreator.core.readFileAsString
import de.dertyp7214.rboard_themecreator.helpers.Decompress
import de.dertyp7214.rboard_themecreator.json.JsonParser
import de.dertyp7214.rboard_themecreator.themes.CssFileReader
import de.dertyp7214.rboard_themecreator.themes.LightGreen
import de.dertyp7214.rboard_themecreator.themes.ThemeManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n", "RtlHardcoded", "SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager = ThemeManager(keyboard, LightGreen(this), this)

        border.setOnCheckedChangeListener { _, isChecked ->
            manager.border = isChecked
            manager.applyTheme()
        }

        val fileName = intent?.extras?.getString("fileName")

        val file = File("/sdcard/Documents/gboardTheme/")
        file.deleteRecursively()

        if (fileName != null && file.mkdirs()) {
            Thread {
                Decompress(fileName, file.absolutePath).unzip()

                val metadata = JsonParser().toClass(
                    JSONObject(readFileAsString(File(file, "metadata.json").absolutePath)),
                    de.dertyp7214.rboard_themecreator.json.Metadata::class.java
                )

                if (metadata?.style_sheets != null && metadata.style_sheets.isNotEmpty() && metadata.style_sheets[0] != null) {
                    val list =
                        CssFileReader(readFileAsString(File(file, metadata.style_sheets[0]!!).absolutePath)).getCssFile()

                    runOnUiThread {
                        CssDefAdapter(this, rv, list.defList)
                    }
                }
            }.start()
        }
    }
}
