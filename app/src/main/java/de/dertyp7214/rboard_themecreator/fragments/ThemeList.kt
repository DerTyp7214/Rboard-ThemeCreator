package de.dertyp7214.rboard_themecreator.fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.adapter.ThemeAdapter
import de.dertyp7214.rboard_themecreator.core.dp
import de.dertyp7214.rboard_themecreator.screens.ThemeOverview
import java.io.File
import java.util.*

class ThemeList(private val scrollState: Int = 0) : BaseFragment() {

    private var elevate = false
    private lateinit var activity: AppCompatActivity
    private lateinit var adapter: ThemeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_theme_list, container, false)

        activity = getActivity()!! as AppCompatActivity

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) activity.window.navigationBarDividerColor =
            resources.getColor(R.color.graySemi, null)

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
            Collections.sort(themes, object : Comparator<GboardTheme> {
                override fun compare(o1: GboardTheme?, o2: GboardTheme?): Int {
                    return (o1?.zipFile?.name ?: "").compareTo((o2?.zipFile?.name ?: ""), true)
                }
            })
            ObjectAnimator.ofFloat(v.findViewById<ProgressBar>(R.id.progressBar2), "alpha", 0F).apply {
                duration = 200
                start()
            }
            ObjectAnimator.ofFloat(v.findViewById<RecyclerView>(R.id.rv), "alpha", 1F).apply {
                duration = 400
                start()
            }
            adapter = ThemeAdapter(activity, v.findViewById(R.id.rv), themes, {
                if (it > 30 && !elevate) {
                    elevate = !elevate
                    val animator =
                        ObjectAnimator.ofFloat(activity.supportActionBar, "elevation", 12.dp(activity).toFloat())
                    animator.setAutoCancel(true)
                    animator.duration = 200
                    animator.start()
                } else if (it <= 30 && elevate) {
                    elevate = !elevate
                    val animator = ObjectAnimator.ofFloat(activity.supportActionBar, "elevation", 0F)
                    animator.setAutoCancel(true)
                    animator.duration = 200
                    animator.start()
                }
            }, {
                if (activity is ThemeOverview) (activity as ThemeOverview).themeListScrollState = it
            })
            v.findViewById<RecyclerView>(R.id.rv).scrollToPosition(scrollState)
        }

        return v
    }

    fun filter(query: String) = adapter.filter(query)
}

data class GboardTheme(val zipFile: File, val image: File?) {

    private var bitmap: Bitmap? = null

    override fun toString(): String {
        return "${javaClass.name} zipFile: ${zipFile.absolutePath} image: ${image?.absolutePath ?: "null"}"
    }

    fun getImage(context: Context): Bitmap? {
        return if (image != null) {
            if (bitmap == null) {
                val tmp = BitmapFactory.decodeFile(
                    image.absolutePath,
                    BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.ARGB_8888 })
                bitmap = Bitmap.createScaledBitmap(
                    tmp,
                    (60.dp(context).toFloat() / tmp.height * tmp.width).toInt(), 60.dp(context), false
                )
            }
            bitmap
        } else null
    }
}