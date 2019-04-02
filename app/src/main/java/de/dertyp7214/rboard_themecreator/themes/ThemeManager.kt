package de.dertyp7214.rboard_themecreator.themes

import android.content.Context
import de.dertyp7214.rboard_themecreator.keyboard.Keyboard

class ThemeManager(val keyboard: Keyboard, var theme: Theme, val context: Context) {

    init {
        applyTheme()
    }

    var backgroundColor = theme.backgroundColor
        set(value) {
            theme.backgroundColor = value
            field = value
        }

    var backgroundImage = theme.backgroundImage
        set(value) {
            theme.backgroundImage = value
            field = value
        }

    var border = theme.border
        set(value) {
            theme.border = value
            field = value
        }

    fun applyTheme() {
        keyboard.setTheme(theme)
    }
}