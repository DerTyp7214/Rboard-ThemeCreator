package de.dertyp7214.rboard_themecreator.adapter

import android.content.Context
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
import de.dertyp7214.rboard_themecreator.core.isHexColor
import de.dertyp7214.rboard_themecreator.themes.CssDef

class CssDefAdapter(
    private val context: Context,
    recyclerView: RecyclerView,
    private val list: List<CssDef>
) :
    RecyclerView.Adapter<CssDefAdapter.ViewHolder>() {

    init {
        recyclerView.adapter = this
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.css_def, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.key.text = item.key
        holder.hex.text = item.value
        if (item.value.isHexColor()) {
            val color = Color.parseColor(toggleCssAndroidAlpha(item.value))
            setColor(holder.color, color)
            holder.main.setOnClickListener {
                ColorPicker(context).apply {
                    colorMode = ColorMode.ARGB
                    setColor(color)
                    setAnimationTime(200)
                    setListener(object : ColorPicker.Listener {
                        override fun update(color: Int) {}
                        override fun cancel() {}
                        override fun color(color: Int) {}

                        override fun color(color: String) {
                            this@CssDefAdapter.setColor(holder.color, Color.parseColor(color))
                            list[position].value = toggleCssAndroidAlpha(color, true).toUpperCase()
                            notifyDataSetChanged()
                        }
                    })
                    show()
                }
            }
        }
    }

    private fun setColor(v: View, color: Int) {
        val drawable = v.background as LayerDrawable
        val gradientDrawable = drawable.findDrawableByLayerId(R.id.plate_color) as GradientDrawable
        gradientDrawable.setColor(color)
    }

    private fun toggleCssAndroidAlpha(hex: String, toCss: Boolean = false): String {
        return if (hex.removePrefix("#").length == 8) "#${hex.removePrefix("#").substring(
            if (toCss) 2 else 6,
            8
        )}${hex.removePrefix("#").substring(0, if (toCss) 2 else 6)}" else "#${hex.removePrefix("#")}"
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val color: View = v.findViewById(R.id.color)
        val hex: TextView = v.findViewById(R.id.hex)
        val key: TextView = v.findViewById(R.id.key)
        val main: ViewGroup = v.findViewById(R.id.main)
    }
}