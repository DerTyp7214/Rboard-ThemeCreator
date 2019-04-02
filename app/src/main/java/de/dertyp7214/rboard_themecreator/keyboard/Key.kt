package de.dertyp7214.rboard_themecreator.keyboard

import android.content.Context
import android.graphics.Color
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.core.dp

open class Key(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    protected lateinit var letterTextView: TextView
    private lateinit var smallLetterTextView: TextView
    protected lateinit var cardView: CardView

    open var letter: String = "A"
        set(value) {
            field = value
            letterTextView.text = value
        }
    open var smallLetter: String = "A"
        set(value) {
            field = value
            smallLetterTextView.text = value
        }
    var cornerRadius: Int = 7.dp(context)
        set(value) {
            field = value
            if (keyBorder)
                cardView.radius = value.toFloat()
        }
    var elevation: Int = 2.dp(context)
        set(value) {
            field = value
            if (keyBorder)
                cardView.elevation = value.toFloat()
        }
    var keyBorder: Boolean = true
        set(value) {
            field = value
            ChangeBounds().apply {
                if (value) {
                    cardView.radius = 7.dp(context).toFloat()
                    cardView.elevation = 2.dp(context).toFloat()
                    cardView.setCardBackgroundColor(Color.WHITE)
                } else {
                    cardView.radius = 0F
                    cardView.elevation = 0F
                    cardView.setCardBackgroundColor(Color.TRANSPARENT)
                }
                startDelay = 0
                interpolator = AccelerateDecelerateInterpolator()
                duration = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
                TransitionManager.beginDelayedTransition(cardView, this)
            }
        }
    var onPress: (key: String) -> Unit = {}
        set(value) {
            field = value
            cardView.setOnClickListener {
                value(letter)
            }
        }
    var keyColor: Int = Color.WHITE
        set(value) {
            field = value
            if (keyBorder) {
                if (Color.alpha(value) < 255) elevation = 0
                cardView.setCardBackgroundColor(value)
            }
        }
    var keyTextColor: Int = Color.BLACK
        set(value) {
            field = value
            letterTextView.setTextColor(value)
        }
    var keySmallTextColor: Int = Color.DKGRAY
        set(value) {
            field = value
            smallLetterTextView.setTextColor(value)
        }

    constructor(context: Context) : this(context, null)

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Key)
            letter = typedArray.getString(R.styleable.Key_key) ?: "A"
            Log.d("KEY", letter)
            cornerRadius = typedArray.getInt(R.styleable.Key_keyCornerRadius, 7.dp(context))
            elevation = typedArray.getInt(R.styleable.Key_keyElevation, 2.dp(context))
            typedArray.recycle()
        }
        init()
    }

    open fun init() {
        View.inflate(context, R.layout.keyboard_key, this)

        letterTextView = findViewById(R.id.letter)
        smallLetterTextView = findViewById(R.id.small_letter)
        cardView = findViewById(R.id.card)

        cardView.radius = cornerRadius.toFloat()
        cardView.elevation = elevation.toFloat()
        letterTextView.text = letter.toLowerCase()
        smallLetterTextView.text = smallLetter.toLowerCase()
    }
}