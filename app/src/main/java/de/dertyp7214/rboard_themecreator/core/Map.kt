package de.dertyp7214.rboard_themecreator.core

import java.util.*

fun <K, V> Map<K, V>.forEachMapped(run: (k: K, v: V) -> Unit) {
    for (entry in entries) {
        val k: K
        val v: V
        try {
            k = entry.key
            v = entry.value
        } catch (ise: IllegalStateException) {
            // this usually means the entry is no longer in the map.
            throw ConcurrentModificationException(ise)
        }

        run(k, v)
    }
}