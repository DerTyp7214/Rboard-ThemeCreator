@file:Suppress("UNCHECKED_CAST")

package de.dertyp7214.rboard_themecreator.json

import org.json.JSONArray
import org.json.JSONObject

private fun JSONArray.forEach(run: (array: JSONArray, index: Int) -> Unit) {
    for (i in 0 until length())
        run(this, i)
}

class SafeJSONObject(s: String) : JSONObject(s) {
    constructor(obj: JSONObject): this(obj.toString())

    override fun getString(name: String?): String {
        return if (has(name)) super.getString(name) else ""
    }

    override fun getInt(name: String?): Int {
        return if (has(name)) super.getInt(name) else 0
    }

    override fun getBoolean(name: String?): Boolean {
        return if (has(name)) super.getBoolean(name) else false
    }

    override fun getJSONArray(name: String?): JSONArray {
        return if (has(name)) super.getJSONArray(name) else JSONArray()
    }
}

class JsonParser {
    fun toJson(clazz: Any?): JSONObject {
        when (clazz) {
            is Metadata -> {
                val obj = JSONObject()
                obj.put("format_version", clazz.format_version)
                obj.put("id", clazz.id)
                obj.put("name", clazz.name)
                obj.put("prefer_key_border", clazz.prefer_key_border)
                obj.put("is_light_theme", clazz.is_light_theme)
                obj.put("style_sheets", toJsonArray(clazz.style_sheets))
                obj.put("flavors", toJsonArray(clazz.flavors))
                return obj
            }
            is Flavor -> {
                val obj = JSONObject()
                obj.put("type", clazz.type)
                obj.put("style_sheets", toJsonArray(clazz.style_sheets))
                return obj
            }
            else -> return JSONObject()
        }
    }

    fun toJsonArray(list: ArrayList<*>): JSONArray {
        if (list.isEmpty()) return JSONArray()
        when (list[0]) {
            is Flavor -> {
                val array = JSONArray()
                list.forEach {
                    array.put(toJson(it))
                }
                return array
            }
            is String -> {
                val array = JSONArray()
                list.forEach {
                    array.put(it)
                }
                return array
            }
            else -> return JSONArray()
        }
    }

    fun <T> toClass(j: JSONObject, clazz: Class<T>): T? {
        val json = SafeJSONObject(j)
        return when (clazz) {
            Metadata::class.java -> {
                val metadata = Metadata()
                metadata.format_version = json.getInt("format_version")
                metadata.id = json.getString("id")
                metadata.name = json.getString("name")
                metadata.prefer_key_border = json.getBoolean("prefer_key_border")
                metadata.is_light_theme = json.getBoolean("is_light_theme")
                metadata.style_sheets = toClassList(json.getJSONArray("style_sheets"), String::class.java)
                metadata.flavors = toClassList(json.getJSONArray("flavors"), Flavor::class.java)
                metadata as T
            }
            Flavor::class.java -> {
                val flavor = Flavor()
                flavor.type = json.getString("type")
                flavor.style_sheets = toClassList(json.getJSONArray("style_sheets"), String::class.java)
                flavor as T
            }
            else -> null
        }
    }

    fun <T> toClassList(json: JSONArray, clazz: Class<T>): ArrayList<T?> {
        return when (clazz) {
            Flavor::class.java -> {
                val array = ArrayList<Flavor?>()
                json.forEach { a, i ->
                    array.add(toClass(a[i] as JSONObject, clazz))
                }
                array as ArrayList<T?>
            }
            String::class.java -> {
                val array = ArrayList<String?>()
                json.forEach { a, i ->
                    array.add(a[i] as String)
                }
                array as ArrayList<T?>
            }
            else -> ArrayList()
        }
    }
}