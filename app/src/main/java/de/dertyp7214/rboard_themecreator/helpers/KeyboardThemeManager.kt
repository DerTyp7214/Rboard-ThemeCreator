package de.dertyp7214.rboard_themecreator.helpers

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.devs.vectorchildfinder.VectorChildFinder
import de.dertyp7214.rboard_themecreator.R

object KeyboardThemeManager {
    var colorA1 = Color.RED
    var colorA2 = Color.RED
    var colorA3 = Color.RED
    var colorA4 = Color.RED
    var colorA5 = Color.RED
    var colorB1 = Color.RED
    var colorB180 = Color.RED
    var colorB2 = Color.RED
    var colorB3 = Color.RED
    var colorB4 = Color.RED
    var colorB5 = Color.RED
    var colorC1 = Color.RED
    var colorC2 = Color.RED
    var colorC3 = Color.RED
    var colorC4 = Color.RED
    var colorC5 = Color.RED
    var colorD1 = Color.RED

    fun getKeyBoardDrawable(
        activity: Activity,
        imageView: ImageView,
        blank: Boolean = false,
        callback: (navColor: Int) -> Unit = {}
    ) {
        val vector = VectorChildFinder(activity, R.drawable.dummy_keyboard, imageView)

        if (!blank) {
            val labels = ArrayList<String>()
            for (i in 1..32) labels.add("label$i")
            val secondaryLabels = ArrayList<String>()
            for (i in 1..10) secondaryLabels.add("labelSecondary$i")
            val colorSetA1 = arrayOf("color_set_a1_1")
            val colorSetA2 = arrayOf("header")
            val colorSetA3 = arrayOf("background")
            val colorSetA4 = arrayOf("")
            val colorSetA5 = arrayOf("")
            val colorSetB1 = labels.toTypedArray()
            val colorSetB180 = secondaryLabels.toTypedArray()
            val colorSetB2 = arrayOf("del1", "del2", "del3", "emoijs", "shift")
            val colorSetB3 = arrayOf("")
            val colorSetB4 = arrayOf("separatorTop")
            val colorSetB5 = arrayOf("")
            val colorSetC1 = arrayOf("spaceBar")
            val colorSetC2 = arrayOf("")
            val colorSetC3 = arrayOf("")
            val colorSetC4 = arrayOf("")
            val colorSetC5 = arrayOf("")
            val colorSetD1 = arrayOf("")

            val map = mapOf(
                Pair("A1", Pair(colorA1, colorSetA1)),
                Pair("A2", Pair(colorA2, colorSetA2)),
                Pair("A3", Pair(colorA3, colorSetA3)),
                Pair("A4", Pair(colorA4, colorSetA4)),
                Pair("A5", Pair(colorA5, colorSetA5)),
                Pair("B1", Pair(colorB1, colorSetB1)),
                Pair("B180", Pair(colorB180, colorSetB180)),
                Pair("B2", Pair(colorB2, colorSetB2)),
                Pair("B3", Pair(colorB3, colorSetB3)),
                Pair("B4", Pair(colorB4, colorSetB4)),
                Pair("B5", Pair(colorB5, colorSetB5)),
                Pair("C1", Pair(colorC1, colorSetC1)),
                Pair("C2", Pair(colorC2, colorSetC2)),
                Pair("C3", Pair(colorC3, colorSetC3)),
                Pair("C4", Pair(colorC4, colorSetC4)),
                Pair("C5", Pair(colorC5, colorSetC5)),
                Pair("D1", Pair(colorD1, colorSetD1))
            )

            map.entries.forEach {
                if (it.key == "A4") {
                    callback(it.value.first)
                }
                it.value.second.forEach { name ->
                    if (name.isNotBlank() && name != "shift") vector.findPathByName(name).fillColor = it.value.first
                    else if (name == "shift") vector.findPathByName(name).strokeColor = it.value.first
                }
            }
        }
        imageView.invalidate()
        resize(activity, imageView)
    }

    private fun resize(activity: Activity, imageView: ImageView) {
        val bmp = imageView.drawable.toBitmap()
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val ratio = size.x.toFloat() / bmp.width.toFloat()
        val width = bmp.width * ratio
        val height = bmp.height * ratio
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, width.toInt(), height.toInt(), false))
    }
}