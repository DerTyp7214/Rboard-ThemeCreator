package de.dertyp7214.rboard_themecreator.themes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color

class PewDiePie(private val context: Context) :
    Theme(Color.RED, null, false, Color.WHITE, Color.WHITE, Color.WHITE, Color.parseColor("#A0000000"), Color.BLACK) {

    override var backgroundImage: Bitmap? = null
        get() {
            if (field == null) {
                val stream = context.assets.open("background")
                field = BitmapFactory.decodeStream(stream)
            }
            return field
        }
}