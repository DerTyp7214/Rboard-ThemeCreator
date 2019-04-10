package de.dertyp7214.rboard_themecreator.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.colorpicker.ColorMode
import de.dertyp7214.rboard_themecreator.colorpicker.ColorPicker
import de.dertyp7214.rboard_themecreator.components.Dialog
import de.dertyp7214.rboard_themecreator.core.dp
import de.dertyp7214.rboard_themecreator.core.isHexColor
import de.dertyp7214.rboard_themecreator.core.setMargins
import de.dertyp7214.rboard_themecreator.core.toggleCssAndroidAlpha
import de.dertyp7214.rboard_themecreator.themes.CssDef

class CssDefAdapter(
    private val activity: Activity,
    recyclerView: RecyclerView,
    private val list: List<CssDef>,
    private val onChange: (key: String, value: Any) -> Unit = { _, _ -> }
) :
    RecyclerView.Adapter<CssDefAdapter.ViewHolder>() {

    init {
        recyclerView.adapter = this
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
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
                            onChange(item.key, list[position].value)
                        }
                    })
                    show()
                }
            }
        } else {
            setColor(holder.color, Color.TRANSPARENT)
            holder.main.setOnClickListener {
                Dialog(activity, "Title", item.key, item.value) {
                    list[position].value = it
                    onChange(item.key, list[position].value)
                }
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