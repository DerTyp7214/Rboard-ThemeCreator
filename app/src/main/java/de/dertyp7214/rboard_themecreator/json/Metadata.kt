package de.dertyp7214.rboard_themecreator.json

data class Metadata(
    var format_version: Int = 0,
    var id: String = "",
    var name: String = "",
    var prefer_key_border: Boolean = false,
    var is_light_theme: Boolean = true,
    var style_sheets: ArrayList<String?> = ArrayList(),
    var flavors: ArrayList<Flavor?> = ArrayList()
)