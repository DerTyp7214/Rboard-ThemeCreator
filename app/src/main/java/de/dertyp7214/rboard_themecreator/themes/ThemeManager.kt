package de.dertyp7214.rboard_themecreator.themes

import android.content.Context
import android.graphics.Color
import android.util.Log
import de.dertyp7214.rboard_themecreator.keyboard.Keyboard

class ThemeManager(val keyboard: Keyboard, theme: Theme?, val context: Context) {

    var theme: Theme = theme ?: Theme(
        Color.TRANSPARENT,
        null,
        false,
        Color.TRANSPARENT,
        Color.TRANSPARENT,
        Color.TRANSPARENT,
        Color.TRANSPARENT,
        Color.TRANSPARENT
    )

    init {
        applyTheme()
    }

    var backgroundColor = theme?.backgroundColor ?: Color.TRANSPARENT
        set(value) {
            theme.backgroundColor = value
            field = value
        }

    var backgroundImage = theme?.backgroundImage
        set(value) {
            theme.backgroundImage = value
            field = value
        }

    var border = theme?.border ?: false
        set(value) {
            theme.border = value
            field = value
        }

    var functionKeyTextColor = theme?.functionKeyTextColor ?: Color.TRANSPARENT
        set(value) {
            theme.functionKeyTextColor = value
            field = value
        }

    var keyTextColor = theme?.keyTextColor ?: Color.TRANSPARENT
        set(value) {
            theme.keyTextColor = value
            field = value
        }

    var keySmallTextColor = theme?.keySmallTextColor ?: Color.TRANSPARENT
        set(value) {
            theme.keySmallTextColor = value
            field = value
        }

    var functionKeyBackgroundColor = theme?.functionKeyBackgroundColor ?: Color.TRANSPARENT
        set(value) {
            theme.functionKeyBackgroundColor = value
            field = value
        }

    var keyBackgroundColor = theme?.keyBackgroundColor ?: Color.TRANSPARENT
        set(value) {
            theme.keyBackgroundColor = value
            field = value
        }

    fun applyTheme() {
        keyboard.setTheme(if (theme.isBlank) null else theme)
        Log.d("BLANK", "${theme.isBlank}")
    }
}