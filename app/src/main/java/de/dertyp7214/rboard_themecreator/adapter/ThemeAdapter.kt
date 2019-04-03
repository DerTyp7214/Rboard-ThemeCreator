package de.dertyp7214.rboard_themecreator.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.dertyp7214.rboard_themecreator.MainActivity
import de.dertyp7214.rboard_themecreator.R
import de.dertyp7214.rboard_themecreator.components.MaskedImageView
import de.dertyp7214.rboard_themecreator.core.dp
import de.dertyp7214.rboard_themecreator.core.getNavigationBarHeight
import de.dertyp7214.rboard_themecreator.core.setMargins
import de.dertyp7214.rboard_themecreator.screens.GboardTheme

class ThemeAdapter(
    private val activity: Activity,
    recyclerView: RecyclerView,
    private val list: ArrayList<GboardTheme>,
    private val onScroll: (position: Int) -> Unit = {}
) :
    RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {

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
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.theme, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.image.setImageBitmap(item.getImage(activity))
        holder.title.text = item.zipFile.name.removeSuffix(".zip")
        holder.card.setOnClickListener {
            activity.startActivity(Intent(activity, MainActivity::class.java).apply {
                putExtra("fileName", item.zipFile.absolutePath)
            })
        }

        (holder.card.parent as ViewGroup).apply {
            val m = 4.dp(context)
            if (position == list.size - 1) setMargins(m, m, m, m + context.getNavigationBarHeight())
            else setMargins(m)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.textView)
        val image: MaskedImageView = v.findViewById(R.id.image)
        val card: LinearLayout = v.findViewById(R.id.card)
    }
}