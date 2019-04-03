package de.dertyp7214.rboard_themecreator.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.colorpicker.ColorMode
import de.dertyp7214.rboard_themecreator.colorpicker.ColorPicker
import de.dertyp7214.rboard_themecreator.core.*
import de.dertyp7214.rboard_themecreator.themes.CssDef

class CssDefAdapter(
    private val activity: Activity,
    recyclerView: RecyclerView,
    private val list: List<CssDef>,
    private val onScroll: (position: Int) -> Unit = {}
) :
    RecyclerView.Adapter<CssDefAdapter.ViewHolder>() {

    init {
        recyclerView.adapter = this
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScroll(recyclerView.computeVerticalScrollOffset())
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.css_def, parent, false))
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.key.text = item.key
        holder.hex.text = item.value

        holder.main.apply {
            val m = 0
            if (position == list.size - 1) setMargins(m, m, m, m + 50.dp(context))
            else setMargins(m)
        }

        if (item.value.isHexColor()) {
            val color = Color.parseColor(item.value.toggleCssAndroidAlpha())
            setColor(holder.color, color)
            holder.main.setOnClickListener {
                ColorPicker(activity).apply {
                    colorMode = ColorMode.ARGB
                    setColor(color)
                    setAnimationTime(200)
                    setListener(object : ColorPicker.Listener {
                        override fun update(color: Int) {}
                        override fun cancel() {}
                        override fun color(color: Int) {}

                        override fun color(color: String) {
                            this@CssDefAdapter.setColor(holder.color, Color.parseColor(color))
                            list[position].value = color.toggleCssAndroidAlpha(true).toUpperCase()
                            notifyDataSetChanged()
                        }
                    })
                    show()
                }
            }
        } else {
            setColor(holder.color, Color.TRANSPARENT)
            holder.main.setOnClickListener {
                val builder = AlertDialog.Builder(activity)
                val dialogView = activity.layoutInflater.inflate(R.layout.popup, null)
                builder.setView(dialogView)
                val editText: TextInputEditText = dialogView.findViewById(R.id.textInput)
                val textInputLayout: TextInputLayout = dialogView.findViewById(R.id.textInputLayout)
                val btnOk: MaterialButton = dialogView.findViewById(R.id.ok)
                val btnCancel: MaterialButton = dialogView.findViewById(R.id.cancel)
                val title: TextView = dialogView.findViewById(R.id.title)
                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                val dimensions = activity.getDimensions()
                dialogView.layoutParams =
                    FrameLayout.LayoutParams((dimensions.first * .8F).toInt(), WRAP_CONTENT).apply {
                        gravity = Gravity.CENTER
                        setMargins(50.dp(activity))
                    }

                title.text = "Title"
                textInputLayout.hint = item.key
                editText.setText(item.value)
                btnOk.isEnabled = false

                editText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        btnOk.isEnabled = s.toString() != item.value
                    }
                })

                btnOk.setOnClickListener {
                    list[position].value = editText.text.toString()
                    dialog.dismiss()
                }
                btnCancel.setOnClickListener { dialog.dismiss() }
            }
        }
    }

    private fun setColor(v: View, color: Int) {
        val drawable = v.background as LayerDrawable
        val gradientDrawable = drawable.findDrawableByLayerId(R.id.plate_color) as GradientDrawable
        gradientDrawable.setColor(color)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val color: View = v.findViewById(R.id.color)
        val hex: TextView = v.findViewById(R.id.hex)
        val key: TextView = v.findViewById(R.id.key)
        val main: ViewGroup = v.findViewById(R.id.main)
    }
}