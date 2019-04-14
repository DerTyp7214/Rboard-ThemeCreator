package de.dertyp7214.rboard_themecreator.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.nbsp.materialfilepicker.MaterialFilePicker
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.adapter.CssDefAdapter
import de.dertyp7214.rboard_themecreator.components.Dialog
import de.dertyp7214.rboard_themecreator.components.ImageBottomSheet
import de.dertyp7214.rboard_themecreator.core.*
import de.dertyp7214.rboard_themecreator.helpers.Compress
import de.dertyp7214.rboard_themecreator.helpers.Decompress
import de.dertyp7214.rboard_themecreator.helpers.KeyboardThemeManager
import de.dertyp7214.rboard_themecreator.helpers.KeyboardThemeManager.getKeyBoardDrawable
import de.dertyp7214.rboard_themecreator.json.JsonParser
import de.dertyp7214.rboard_themecreator.screens.ThemeOverview
import de.dertyp7214.rboard_themecreator.themes.CssDef
import de.dertyp7214.rboard_themecreator.themes.CssFileReader
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.util.regex.Pattern

class ThemeEditor(private val fileName: String? = "", private val key: String? = "") : BaseFragment() {

    private var elevate = false
    private var exported = true
    private var defList = ArrayList<CssDef>()
    private var navColor = Color.WHITE
    private var baseColor: Int = Color.WHITE
    private lateinit var activity: AppCompatActivity
    private lateinit var behavior: BottomSheetBehavior<View>
    var bottomSheetImageView: ImageView? = null

    @SuppressLint("SdCardPath", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_theme_editor, container, false)

        activity = getActivity() as AppCompatActivity

        navColor = activity.window.navigationBarColor
        baseColor = activity.window.navigationBarColor

        if (activity is ThemeOverview) (activity as ThemeOverview).themeEditorInstance = this

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) activity.window.navigationBarDividerColor =
            Color.TRANSPARENT

        val keyboard: ImageView = v.findViewById(R.id.keyboard)

        val bottomSheet: LinearLayout = v.findViewById(R.id.bottom_sheet)

        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.isHideable = false

        getKeyBoardDrawable(activity, keyboard, true)
        keyboard.setOnClickListener { }
        v.findViewById<ViewGroup>(R.id.ll).setOnClickListener {
            if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) behavior.state =
                BottomSheetBehavior.STATE_EXPANDED
            else if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) behavior.state =
                BottomSheetBehavior.STATE_COLLAPSED
        }

        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) {}
            override fun onSlide(p0: View, p1: Float) {
                v.findViewById<ImageView>(R.id.bottomSheetArrow).rotation = 180 * p1
                v.findViewById<ProgressBar>(R.id.progressBarBottomSheet).progress = (1000 * p1).toInt()
                activity.window.navigationBarColor = ColorUtils.blendARGB(baseColor, navColor, p1)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ColorUtils.calculateLuminance(activity.window.navigationBarColor) > .5F)
                        activity.window.decorView.systemUiVisibility =
                            activity.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    else
                        activity.window.decorView.systemUiVisibility =
                            activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
                }
            }
        })

        val file = File("/sdcard/Documents/gboardTheme/")
        file.deleteRecursively()

        activity.title = File(fileName).name.removeSuffix(".zip")

        if (fileName != null && file.mkdirs()) {
            Thread {
                Thread.sleep(250)
                try {
                    Decompress(fileName, file.absolutePath).unzip {
                        activity.runOnUiThread { v.findViewById<TextView>(R.id.progress).text = "$it%" }
                    }

                    if (fileName == "/sdcard/Documents/thememe.zip") File(fileName).delete()

                    val metadata = JsonParser().toClass(
                        JSONObject(readFileAsString(File(file, "metadata.json").absolutePath)),
                        de.dertyp7214.rboard_themecreator.json.Metadata::class.java
                    )

                    if (metadata?.style_sheets != null && metadata.style_sheets.contains("style_sheet_variables.css")) {
                        val styleSheet =
                            File(file, metadata.style_sheets.findLast { it == "style_sheet_variables.css" })
                        val list =
                            CssFileReader(readFileAsString(styleSheet.absolutePath)).getCssFile()

                        v.findViewById<Switch>(R.id.lightTheme).setOnCheckedChangeListener { _, isChecked ->
                            metadata.is_light_theme = isChecked
                        }

                        if (key != null && key.isNotBlank()) {
                            val ac = activity
                            if (ac is ThemeOverview && ac.tmpThemes.containsKey(key)) {
                                list.defList.clear()
                                list.defList.addAll(ac.tmpThemes[key]!!.second)
                                activity.runOnUiThread { ac.title = key }
                            }
                        }

                        activity.runOnUiThread {
                            defList = list.defList
                            list.defList.mapKeyBoardThemeManager(KeyboardThemeManager)
                            getKeyBoardDrawable(activity, keyboard) {
                                navColor = it
                                if (behavior.state == BottomSheetBehavior.STATE_EXPANDED)
                                    activity.window.navigationBarColor = it
                            }
                            if (activity is ThemeOverview) (activity as ThemeOverview).toolbar.getTextViewTitle()?.setOnClickListener {
                                Dialog(
                                    activity,
                                    "Title",
                                    "Theme Name",
                                    activity.title.toString(),
                                    getString(R.string.inputFilter)
                                ) {
                                    activity.title = it
                                }
                            }
                            v.findViewById<MaterialButton>(R.id.btnBackground).setOnClickListener {
                                ImageBottomSheet(activity).apply {
                                    setImage(File(file, "background").bitmap) {
                                        bottomSheetImageView = imageView
                                        MaterialFilePicker()
                                            .withActivity(this@ThemeEditor.activity)
                                            .withRequestCode(11)
                                            .withFilter(Pattern.compile(".*\\.(png|jpg|jpeg)$"))
                                            .start()
                                    }
                                    show(this@ThemeEditor.activity.supportFragmentManager, "")
                                }
                            }
                            v.findViewById<Button>(R.id.button).setOnClickListener {
                                styleSheet.delete()
                                list.getNewCss().writeToFile(styleSheet)
                                metadata.id = getString(R.string.base_id, activity.title.toString())
                                metadata.name = activity.title.toString()
                                val json = JsonParser().toJson(metadata)
                                val output = BufferedWriter(FileWriter(File(file, "metadata.json")))
                                output.write(json.toString(4))
                                output.close()
                                val zip = File("/sdcard/Documents/${metadata.name}.zip")
                                Compress().zip(file.getFileArray(), zip.absolutePath)
                                file.deleteRecursively()
                                exported = true
                                activity.apply {
                                    if (this is ThemeOverview) {
                                        tmpThemes.remove(title.toString())
                                        updateDrawer()
                                        val bitmap = keyboard.drawable.toBitmap()
                                        val preview = File("/sdcard/Documents/${metadata.name}")
                                        val out = FileOutputStream(preview)
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                                        out.flush()
                                        out.close()
                                        "rm /etc/gboard_theme/thememe.zip".runAsCommand()
                                        "mv ${preview.absolutePath} /etc/gboard_theme/${preview.name}".runAsCommand()
                                        if ("mv ${zip.absolutePath} /etc/gboard_theme/${zip.name}".runAsCommand()) {
                                            Toast.makeText(activity, R.string.theme_exported, Toast.LENGTH_SHORT).show()
                                            setHome(false)
                                        } else
                                            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            v.findViewById<NestedScrollView>(R.id.scrollView)
                                .setOnScrollChangeListener { _: NestedScrollView?, _: Int, y: Int, _: Int, _: Int ->
                                    if (y != 0 && !elevate) {
                                        elevate = !elevate
                                        val animator = ObjectAnimator.ofFloat(
                                            activity.supportActionBar,
                                            "elevation",
                                            12.dp(activity).toFloat()
                                        )
                                        animator.setAutoCancel(true)
                                        animator.duration = 200
                                        animator.start()
                                    } else if (y == 0) {
                                        elevate = !elevate
                                        val animator =
                                            ObjectAnimator.ofFloat(activity.supportActionBar, "elevation", 0F)
                                        animator.setAutoCancel(true)
                                        animator.duration = 200
                                        animator.start()
                                    }
                                }
                            CssDefAdapter(activity, v.findViewById(R.id.rv), list.defList) { _, _ ->
                                list.defList.mapKeyBoardThemeManager(KeyboardThemeManager)
                                getKeyBoardDrawable(activity, keyboard) {
                                    navColor = it
                                    if (behavior.state == BottomSheetBehavior.STATE_EXPANDED)
                                        activity.window.navigationBarColor = it
                                }
                                exported = false
                                defList = list.defList
                            }
                            Handler().postDelayed({
                                ObjectAnimator.ofFloat(v.findViewById<FrameLayout>(R.id.progressBarBox), "alpha", 0F)
                                    .apply {
                                        duration = 200
                                        start()
                                    }
                                ObjectAnimator.ofFloat(v.findViewById<RecyclerView>(R.id.layout), "alpha", 1F)
                                    .apply {
                                        duration = 700
                                        start()
                                    }
                            }, 250)
                        }
                    } else throw Exception("")
                } catch (e: Exception) {
                    Log.d("EXIT", Log.getStackTraceString(e))
                    if (activity is ThemeOverview) {
                        activity.runOnUiThread {
                            val ac = activity as ThemeOverview
                            Toast.makeText(ac, R.string.not_supported, Toast.LENGTH_SHORT).show()
                            ac.setHome(false)
                        }
                    }
                }
            }.start()
        }
        return v
    }

    override fun onBackPressed(): Boolean {
        if (!exported) {
            val ac = activity
            if (ac is ThemeOverview) {
                ac.tmpThemes[ac.toolbar.title.toString()] = Pair(System.currentTimeMillis(), defList)
                ac.updateDrawer()
            }
        }
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        else return true
        return false
    }
}
