package de.dertyp7214.rboard_themecreator.themes

import android.graphics.Bitmap
import android.graphics.Color

open class Theme(
    var backgroundColor: Int,
    open var backgroundImage: Bitmap?,
    var border: Boolean,
    var functionKeyTextColor: Int,
    var keyTextColor: Int,
    var keySmallTextColor: Int,
    var functionKeyBackgroundColor: Int,
    var keyBackgroundColor: Int
) {
    override fun toString(): String {
        return "backgroundColor: ${Integer.toHexString(backgroundColor)}\n" +
                "border: $border\n" +
                "functionKeyTextColor: ${Integer.toHexString(functionKeyTextColor)}\n" +
                "keyTextColor: ${Integer.toHexString(keyTextColor)}\n" +
                "keySmallTextColor: ${Integer.toHexString(keySmallTextColor)}\n" +
                "functionKeyBackgroundColor: ${Integer.toHexString(functionKeyBackgroundColor)}\n" +
                "keyBackgroundColor: ${Integer.toHexString(keyBackgroundColor)}\n"

    }

    val isBlank: Boolean
        get() {
            return backgroundColor == Color.TRANSPARENT
                    && backgroundImage == null
                    && !border
                    && functionKeyTextColor == Color.TRANSPARENT
                    && keyTextColor == Color.TRANSPARENT
                    && keySmallTextColor == Color.TRANSPARENT
                    && functionKeyBackgroundColor == Color.TRANSPARENT
                    && keyBackgroundColor == Color.TRANSPARENT
        }
}