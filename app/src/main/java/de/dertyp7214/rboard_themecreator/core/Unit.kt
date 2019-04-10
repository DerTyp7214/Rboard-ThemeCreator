package de.dertyp7214.rboard_themecreator.core

import android.os.Handler

fun (() -> Unit).invokeDelay(delay: Long) {
    Handler().postDelayed({
        invoke()
    }, delay)
}

fun ((vararg: Any) -> Any).invokeDelay(delay: Long, vararg: Any = "") {
    Handler().postDelayed({
        invoke(vararg)
    }, delay)
}