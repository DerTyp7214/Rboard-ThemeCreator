package de.dertyp7214.rboard_themecreator.screens

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.adapter.ThemeAdapter
import de.dertyp7214.rboard_themecreator.core.dp
import kotlinx.android.synthetic.main.activity_theme_overview.*
import java.io.File
import java.util.*

class ThemeOverview : AppCompatActivity() {

    private var elevate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_overview)
        setSupportActionBar(toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) window.navigationBarDividerColor =
            resources.getColor(R.color.graySemi, null)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)

        DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withTranslucentStatusBar(false)
            .addDrawerItems(
                PrimaryDrawerItem()
                    .withIdentifier(1)
                    .withSelectable(false)
                    .withSetSelected(false)
                    .withName(R.string.settings)
                    .withIcon(R.drawable.ic_settings)
            )
            .withOnDrawerItemClickListener { _, _, drawerItem ->
                true
            }
            .build()
            .deselect()

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
            ThemeAdapter(this, rv, themes) {
                if (it > 30 && !elevate) {
                    elevate = !elevate
                    val animator = ObjectAnimator.ofFloat(toolbar, "elevation", 12.dp(this).toFloat())
                    animator.setAutoCancel(true)
                    animator.duration = 200
                    animator.start()
                } else if (it <= 30 && elevate) {
                    elevate = !elevate
                    val animator = ObjectAnimator.ofFloat(toolbar, "elevation", 0F)
                    animator.setAutoCancel(true)
                    animator.duration = 200
                    animator.start()
                }
            }
        }
    }
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