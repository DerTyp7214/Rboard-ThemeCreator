package de.dertyp7214.rboard_themecreator.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.core.dp
import de.dertyp7214.rboard_themecreator.core.setMargins

class FunctionKey(context: Context, attrs: AttributeSet?) : Key(context, attrs) {

    override var letter: String = "A"
        set(value) {
            letterTextView.text = value
            field = value
        }
    override var smallLetter: String = "A"

    constructor(context: Context) : this(context, null)

    var normalSize = false
        set(value) {
            width = if (value) 1F else 1.5F
            field = value
        }

    var width = 1.5F
        set(value) {
            field = value
            cardView.layoutParams =
                LayoutParams(((33 * value) + (6 * Math.round(value - 1))).dp(context).toInt(), 45.dp(context))
            cardView.setMargins(3.dp(context))
        }

    override fun init() {
        View.inflate(context, R.layout.keyboard_function_key, this)

        cardView = findViewById(R.id.card)
        letterTextView = findViewById(R.id.letter)

        cardView.radius = cornerRadius.toFloat()
        cardView.elevation = elevation.toFloat()
    }
}