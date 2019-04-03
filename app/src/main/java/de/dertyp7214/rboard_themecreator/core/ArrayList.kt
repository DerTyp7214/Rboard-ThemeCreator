package de.dertyp7214.rboard_themecreator.core

import de.dertyp7214.rboard_themecreator.themes.CssDef

operator fun ArrayList<CssDef>.get(key: String): CssDef {
    return find { it.key == key }!!
}