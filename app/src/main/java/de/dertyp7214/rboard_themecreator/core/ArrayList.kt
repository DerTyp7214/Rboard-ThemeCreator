package de.dertyp7214.rboard_themecreator.core

import android.graphics.Color
import de.dertyp7214.rboard_themecreator.helpers.KeyboardThemeManager
import de.dertyp7214.rboard_themecreator.themes.CssDef

operator fun ArrayList<CssDef>.get(key: String): CssDef {
    return find { it.key == key }!!
}

fun ArrayList<CssDef>.mapKeyBoardThemeManager(manager: KeyboardThemeManager) {
    manager.colorA1 = getColor("color_set_a1")
    manager.colorA2 = getColor("color_set_a2")
    manager.colorA3 = getColor("color_set_a3")
    manager.colorA4 = getColor("color_set_a4")
    manager.colorA5 = getColor("color_set_a5")
    manager.colorB1 = getColor("color_set_b1")
    manager.colorB180 = getColor("color_set_b1_80")
    manager.colorB2 = getColor("color_set_b2")
    manager.colorB3 = getColor("color_set_b3")
    manager.colorB4 = getColor("color_set_b4")
    manager.colorB5 = getColor("color_set_b5")
    manager.colorC1 = getColor("color_set_c1")
    manager.colorC2 = getColor("color_set_c2")
    manager.colorC3 = getColor("color_set_c3")
    manager.colorC4 = getColor("color_set_c4")
    manager.colorC5 = getColor("color_set_c5")
    manager.colorD1 = getColor("color_set_d1")
}

private fun ArrayList<CssDef>.getColor(key: String): Int {
    return try {
        Color.parseColor(get(key).value.split("\n")[0].toggleCssAndroidAlpha())
    } catch (e: Exception) {
        Color.WHITE
    }
}