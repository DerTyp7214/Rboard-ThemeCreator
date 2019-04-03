package de.dertyp7214.rboard_themecreator

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import de.dertyp7214.rboard_themecreator.adapter.CssDefAdapter
import de.dertyp7214.rboard_themecreator.core.dp
import de.dertyp7214.rboard_themecreator.core.readFileAsString
import de.dertyp7214.rboard_themecreator.core.writeToFile
import de.dertyp7214.rboard_themecreator.helpers.Decompress
import de.dertyp7214.rboard_themecreator.json.JsonParser
import de.dertyp7214.rboard_themecreator.themes.CssFileReader
import de.dertyp7214.rboard_themecreator.themes.LightGreen
import de.dertyp7214.rboard_themecreator.themes.ThemeManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {

    private var elevate = false

    @SuppressLint("SetTextI18n", "RtlHardcoded", "SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val manager = ThemeManager(keyboard, LightGreen(this), this)

        val bottomSheet: LinearLayout = findViewById(R.id.bottom_sheet)

        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.isHideable = false

        keyboard.setOnClickListener { }
        ll.setOnClickListener {
            if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) behavior.state =
                BottomSheetBehavior.STATE_EXPANDED
            else if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) behavior.state =
                BottomSheetBehavior.STATE_COLLAPSED
        }

        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) {}
            override fun onSlide(p0: View, p1: Float) {
                bottomSheetArrow.rotation = 180 * p1
                progressBar.progress = (1000 * p1).toInt()
            }
        })

        border.setOnCheckedChangeListener { _, isChecked ->
            manager.border = isChecked
            manager.applyTheme()
        }

        val fileName = intent?.extras?.getString("fileName")

        val file = File("/sdcard/Documents/gboardTheme/")
        file.deleteRecursively()

        title = File(fileName).name.removeSuffix(".zip")

        if (fileName != null && file.mkdirs()) {
            Thread {
                Decompress(fileName, file.absolutePath).unzip()

                val metadata = JsonParser().toClass(
                    JSONObject(readFileAsString(File(file, "metadata.json").absolutePath)),
                    de.dertyp7214.rboard_themecreator.json.Metadata::class.java
                )

                if (metadata?.style_sheets != null && metadata.style_sheets.isNotEmpty() && metadata.style_sheets[0] != null) {
                    val styleSheet = File(file, metadata.style_sheets[0]!!)
                    val list =
                        CssFileReader(readFileAsString(styleSheet.absolutePath)).getCssFile()

                    runOnUiThread {
                        button.setOnClickListener {
                            styleSheet.renameTo(File(file, "${styleSheet.name}.old"))
                            list.getNewCss().writeToFile(styleSheet)
                        }
                        scrollView.setOnScrollChangeListener { _: NestedScrollView?, _: Int, y: Int, _: Int, _: Int ->
                            if (y != 0 && !elevate) {
                                elevate = !elevate
                                val animator = ObjectAnimator.ofFloat(toolbar, "elevation", 12.dp(this).toFloat())
                                animator.setAutoCancel(true)
                                animator.duration = 200
                                animator.start()
                            } else if (y == 0) {
                                elevate = !elevate
                                val animator = ObjectAnimator.ofFloat(toolbar, "elevation", 0F)
                                animator.setAutoCancel(true)
                                animator.duration = 200
                                animator.start()
                                Log.d("SCROLL", "$y ${toolbar.elevation}")
                            }
                        }
                        CssDefAdapter(this, rv, list.defList)
                    }
                }
            }.start()
        }
    }
}