package de.dertyp7214.rboard_themecreator.keyboard

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.get
import androidx.core.graphics.set
import androidx.core.view.setPadding
import de.dertyp7214.rboard_themecreator.core.dp
import de.dertyp7214.rboard_themecreator.core.nextActivity
import de.dertyp7214.rboard_themecreator.themes.Theme

class Keyboard(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var bg: Bitmap? = null
    private var onKeyPress: (key: String) -> Unit = {}
    private val keys = arrayOf(
        arrayOf(
            Pair("1", ""),
            Pair("2", ""),
            Pair("3", ""),
            Pair("4", ""),
            Pair("5", ""),
            Pair("6", ""),
            Pair("7", ""),
            Pair("8", ""),
            Pair("9", ""),
            Pair("0", "")
        ),
        arrayOf(
            Pair("Q", "%"),
            Pair("W", "\\"),
            Pair("E", "|"),
            Pair("R", "="),
            Pair("T", "["),
            Pair("Y", "]"),
            Pair("U", "<"),
            Pair("I", ">"),
            Pair("O", "{"),
            Pair("P", "}")
        ),
        arrayOf(
            Pair("A", "@"),
            Pair("S", "#"),
            Pair("D", "$"),
            Pair("F", "_"),
            Pair("G", "&"),
            Pair("H", "-"),
            Pair("J", "+"),
            Pair("K", "("),
            Pair("L", ")")
        ),
        arrayOf(
            Pair("CAPS", "CAPS"),
            Pair("Z", "*"),
            Pair("X", "\""),
            Pair("C", "'"),
            Pair("V", ":"),
            Pair("B", ";"),
            Pair("N", "!"),
            Pair("M", "?"),
            Pair("DEL", "DEL")
        ),
        arrayOf(
            Pair("SYMBOLS", "?123"),
            Pair("COMMA", ","),
            Pair("SPACE", "SPACE"),
            Pair("DOT", "."),
            Pair("GO", "GO")
        )
    )
    private val keyInstances = ArrayList<Key>()
    private var keyboardColor: Int = Color.parseColor("#e8eaed")
        set(value) {
            field = value
            setBackgroundColor(value)
        }
    private var keyboardImage: Bitmap? = null
        set(value) {
            field = value
            bg = value
            if (value != null) {
                onAttach.add {
                    Thread {
                        val hw = value.height - value.width >= 0
                        val height = if (hw) value.width / width * height else height
                        val width = if (hw) height else value.height / height * width
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        for (w in 0 until width) {
                            for (h in 0 until height) {
                                bitmap[w, h] = value[w, h]
                            }
                        }
                        this@Keyboard.context.nextActivity()?.runOnUiThread {
                            background = bitmap.toDrawable(resources)
                        }
                    }.start()
                }
            }
        }
    private val onAttach = ArrayList<() -> Unit>()

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        onAttach.forEach { it() }
    }

    init {
        orientation = VERTICAL
        setPadding(4.dp(context))
        setBackgroundColor(keyboardColor)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        keys.forEach { row ->
            addView(LinearLayout(context).apply {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                gravity = CENTER
                row.forEach { key ->
                    if (key.first.length > 1) {
                        addView(FunctionKey(context).apply {
                            letter = key.second
                            smallLetter = key.first
                            normalSize = (key.first == "COMMA" || key.first == "DOT")
                            if (key.first == "SPACE") width = (10 - row.size).toFloat()
                            onPress = { onKeyPress(it) }
                            keyInstances.add(this)
                        })
                    } else {
                        addView(Key(context).apply {
                            letter = key.first
                            smallLetter = key.second
                            onPress = { onKeyPress(it) }
                            keyInstances.add(this)
                        })
                    }
                }
            })
        }
    }

    fun setOnKeyPressListener(listener: (key: String) -> Unit) {
        onKeyPress = listener
        keyInstances.forEach { key ->
            key.onPress = { onKeyPress(it) }
        }
    }

    private fun setKeyBorders(border: Boolean) {
        keyInstances.forEach {
            it.keyBorder = border
        }
    }

    fun setCase(upper: Boolean) {
        keyInstances.forEach {
            if (it.smallLetter.length <= 1)
                it.letter = if (upper) it.letter.toUpperCase() else it.letter.toLowerCase()
        }
    }

    private fun setKeyTextColor(color: Int) {
        keyInstances.forEach {
            if (it.smallLetter.length <= 1)
                it.keyTextColor = color
        }
    }

    private fun setKeySmallTextColor(color: Int) {
        keyInstances.forEach {
            if (it.smallLetter.length <= 1)
                it.keySmallTextColor = color
        }
    }

    private fun setKeyColor(color: Int) {
        keyInstances.forEach {
            if (it.smallLetter.length <= 1)
                it.keyColor = color
        }
    }

    private fun setFunctionKeyColor(color: Int) {
        keyInstances.forEach {
            if (it.smallLetter.length > 1)
                it.keyTextColor = color
        }
    }

    private fun setFunctionKeyBackground(color: Int) {
        keyInstances.forEach {
            if (it.smallLetter.length > 1)
                it.keyColor = color
        }
    }

    fun setTheme(theme: Theme?) {
        if (theme != null) {
            setKeyBorders(theme.border)
            if (theme.backgroundImage == null) keyboardColor = theme.backgroundColor
            if (theme.backgroundImage != null && bg != theme.backgroundImage) keyboardImage = theme.backgroundImage
            setFunctionKeyBackground(theme.functionKeyBackgroundColor)
            setFunctionKeyColor(theme.functionKeyTextColor)
            setKeyTextColor(theme.keyTextColor)
            setKeySmallTextColor(theme.keySmallTextColor)
            setKeyColor(theme.keyBackgroundColor)
        }
    }
}