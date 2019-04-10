package de.dertyp7214.rboard_themecreator.themes

class CssFileReader(private val css: String) {

    fun getCssFile(): CssList {
        val parsedDef = parseDef(css)
        return CssList(parsedDef.first, ArrayList(), css)
    }

    private fun parseDef(string: String): Pair<ArrayList<CssDef>, String> {
        val list = ArrayList<CssDef>()
        string.split("@def ").forEach {
            val line = it.split(";")[0]
            line.split(" ").apply {
                try {
                    list.add(CssDef(this[0], this[1]))
                } catch (e: Exception) {
                }
            }
        }
        val s = try {
            val tmp = string.split("@def ").last().split(";")
            tmp.subList(1, tmp.size - 1).joinToString(";")
        } catch (e: Exception) {
            ""
        }
        return Pair(list, s)
    }

    private fun parseCss(string: String): ArrayList<CssEntry> {
        val list = ArrayList<CssEntry>()
        string.split("}\n").forEach {
            val line = it.split("{\n")
            val tags = line[0].split(",\n")
            val entries = line[1].split("\n").filter { str -> str.isNotBlank() }.map { entry ->
                val tmp = entry.trim().split(" ")
                val key = tmp[0].replace(":", "")
                val value = try {
                    tmp[1].split(";")[0]
                } catch (e: Exception) {
                    ""
                }
                CssDef(key, value)
            }
            list.add(CssEntry(tags.toList(), entries.toList()))
        }
        return list
    }
}

data class CssEntry(val keys: List<String>, var value: List<CssDef>)
data class CssDef(val key: String, var value: String)
data class CssList(val defList: ArrayList<CssDef>, val entryList: List<CssEntry>, val original: String) {
    fun getNewCss(): String {
        var tmp = original
        defList.forEach {
            tmp = tmp.replace(Regex("@def.*${it.key} .*;"), "@def ${it.key} ${it.value};")
        }
        return tmp
    }
}